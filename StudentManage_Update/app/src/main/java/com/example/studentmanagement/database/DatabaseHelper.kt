
package com.example.studentmanagement.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.studentmanagement.model.Student

object DBContract {
    const val DATABASE_NAME = "StudentManagement.db"
    const val DATABASE_VERSION = 1 // Tăng version nếu bạn thay đổi cấu trúc bảng từ cũ sang mới

    object StudentTable {
        const val TABLE_NAME = "sinhvien"
        const val COLUMN_CODE = "mssv" // Sẽ là khóa chính
        const val COLUMN_NAME = "hoten"
    }
}

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        // *** THAY ĐỔI: mssv là PRIMARY KEY, không còn cột 'id' tự tăng ***
        val createTableSQL = "CREATE TABLE ${DBContract.StudentTable.TABLE_NAME} (" +
                "${DBContract.StudentTable.COLUMN_CODE} TEXT PRIMARY KEY," +
                "${DBContract.StudentTable.COLUMN_NAME} TEXT NOT NULL);"
        db?.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${DBContract.StudentTable.TABLE_NAME}")
        onCreate(db)
    }


    fun addStudent(student: Student): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(DBContract.StudentTable.COLUMN_CODE, student.studentId)
            put(DBContract.StudentTable.COLUMN_NAME, student.studentName)
        }
        val result = db.insertWithOnConflict(
            DBContract.StudentTable.TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_IGNORE // Trả về -1 nếu MSSV đã tồn tại
        )
        db.close()
        return result
    }


    fun getAllStudentsAsList(): MutableList<Student> {
        val studentList = mutableListOf<Student>()
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            DBContract.StudentTable.TABLE_NAME,
            null, null, null, null, null,
            "${DBContract.StudentTable.COLUMN_NAME} ASC"
        )

        if (cursor.moveToFirst()) {
            val mssvCol = cursor.getColumnIndex(DBContract.StudentTable.COLUMN_CODE)
            val nameCol = cursor.getColumnIndex(DBContract.StudentTable.COLUMN_NAME)

            do {
                val mssv = cursor.getString(mssvCol)
                val name = cursor.getString(nameCol)
                studentList.add(Student(name, mssv))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return studentList
    }


    fun updateStudent(student: Student): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(DBContract.StudentTable.COLUMN_NAME, student.studentName)
        }
        val selection = "${DBContract.StudentTable.COLUMN_CODE} = ?"
        val selectionArgs = arrayOf(student.studentId)
        val count = db.update(DBContract.StudentTable.TABLE_NAME, values, selection, selectionArgs)
        db.close()
        return count
    }


    fun deleteStudent(studentId: String): Int {
        val db = this.writableDatabase
        val selection = "${DBContract.StudentTable.COLUMN_CODE} = ?"
        val selectionArgs = arrayOf(studentId)
        val deletedRows = db.delete(DBContract.StudentTable.TABLE_NAME, selection, selectionArgs)
        db.close()
        return deletedRows
    }
}
