package com.yudi.udrop.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.annotation.Nullable
import com.yudi.udrop.model.local.UserModel
import org.json.JSONArray
import org.json.JSONObject


class SQLiteManager
/**
 * 作为SQLiteOpenHelper子类必须有的构造方法
 * @param context 上下文参数
 * @param name 数据库名字
 * @param factory 游标工厂 ，通常是null
 * @param version 数据库的版本
 */
    (
    @Nullable context: Context?,
    @Nullable name: String?,
    @Nullable factory: CursorFactory?,
    version: Int
) :
    SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table user(id int not null primary key,name varchar(50) not null,motto varchar(1000),days int)")
        db.execSQL("create table new(title varchar(1000) not null primary key,done int not null)")
        db.execSQL("create table review(title varchar(1000) not null primary key,done int not null,time varchar(100))")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun addUser(id: Int, name: String) {
        val contentValues = ContentValues()
        contentValues.put("id", id)
        contentValues.put("name", name)
        contentValues.put("motto", "")
        contentValues.put("days", 0)
        writableDatabase.insert("user", null, contentValues)
        writableDatabase.close()
    }

    fun addNewSchedule(title: String, done: Int): Long {
        val contentValues = ContentValues().apply {
            put("title", title)
            put("done", done)
        }
        val resultCode = writableDatabase.insert("new", null, contentValues)
        writableDatabase.close()
        return resultCode
    }

    fun addReviewSchedule(title: String, done: Int) {
        val contentValues = ContentValues().apply {
            put("title", title)
            put("done", done)
        }
        writableDatabase.insert("review", null, contentValues)
        writableDatabase.close()
    }

    fun updateInfo(name: String, motto: String, days: Int) {
        getInfo()?.let {
            val contentValues = ContentValues()
            contentValues.put("id", it.id)
            contentValues.put("name", name)
            contentValues.put("motto", motto)
            contentValues.put("days", days)
            writableDatabase.update("user", contentValues, "id=?", arrayOf(it.id.toString()))
            writableDatabase.close()
        }

    }

    fun deleteUser() {
        getInfo()?.let {
            writableDatabase.delete("user", "id=?", arrayOf(it.id.toString()))
            writableDatabase.close()
        }
    }

    fun deleteNew(title: String) {
        writableDatabase.delete("new", "title=?", arrayOf(title))
        writableDatabase.close()
    }

    fun deleteReview(title: String) {
        writableDatabase.delete("review", "title=?", arrayOf(title))
        writableDatabase.close()
    }

    fun getInfo(): UserModel? {
        // 参数1：table_name
        // 参数2：columns 要查询出来的列名。相当于 select  *** from table语句中的 ***部分
        // 参数3：selection 查询条件字句，在条件子句允许使用占位符“?”表示条件值
        // 参数4：selectionArgs ：对应于 selection参数 占位符的值
        // 参数5：groupby 相当于 select *** from table where && group by ... 语句中 ... 的部分
        // 参数6：having 相当于 select *** from table where && group by ...having %%% 语句中 %%% 的部分
        // 参数7：orderBy ：相当于 select  ***from ？？  where&& group by ...having %%% order by@@语句中的@@ 部分，如： personid desc（按person 降序）
        val cursor: Cursor = readableDatabase.query("user", null, null, null, null, null, null)
        return try {
            cursor.moveToFirst()
            UserModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3))
        } catch (e: Exception) {
            Log.e("SQLiteManager", e.toString())
            null
        } finally {
            readableDatabase.close()
        }
    }

    fun getNew(): JSONArray {
        val cursor: Cursor = readableDatabase.query("new", null, null, null, null, null, null)
        val newList = arrayListOf<JSONObject>()
        try {
            cursor.moveToFirst()
            newList.add(JSONObject().apply {
                put("title", cursor.getString(0))
                put("done", cursor.getInt(1))
            })
            while (cursor.moveToNext()) {
                newList.add(JSONObject().apply {
                    put("title", cursor.getString(0))
                    put("done", cursor.getInt(1))
                })
            }
        } catch (e: Exception) {
            Log.e("SQLiteManager", e.toString())
        }
        return JSONArray(newList)
    }

    fun getReview(): JSONArray {
        val cursor: Cursor = readableDatabase.query("review", null, null, null, null, null, null)
        val reviewList = arrayListOf<JSONObject>()
        try {
            cursor.moveToFirst()
            reviewList.add(JSONObject().apply {
                put("title", cursor.getString(0))
                put("time", cursor.getString(2))
                put("done", cursor.getInt(1))
            })
            while (cursor.moveToNext()) {
                reviewList.add(JSONObject().apply {
                    put("title", cursor.getString(0))
                    put("time", cursor.getString(2))
                    put("done", cursor.getInt(1))
                })
            }
        } catch (e: Exception) {
            Log.e("SQLiteManager", e.toString())
        }
        return JSONArray(reviewList)
    }
}