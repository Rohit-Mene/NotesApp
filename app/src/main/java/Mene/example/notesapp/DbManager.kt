package Mene.example.notesapp

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.media.projection.MediaProjection
import android.widget.Toast

class DbManager{
    val dbName="MyNotes"
    val dbTable="Notes"
    val colId="ID"
    val colTitle="Title"
    val colDes="Description"
    val dbVersion=1
   // val sqlCreateTable="CREATE TABLE IF NOT EXISTS"+dbTable+"("+colId+"INTEGER PRIMARY KEY,"+
          //  colTitle+"TEXT,"+colDes+"TEXT);"
    val sqlCreateTable="CREATE TABLE IF NOT EXISTS "+ dbTable +" ("+ colId +" INTEGER PRIMARY KEY,"+
            colTitle + " TEXT, "+ colDes +" TEXT);"

 var sqlDB:SQLiteDatabase?=null


    constructor(context: Context){

        var db=DatabaseHelperNotes(context)
        sqlDB=db.writableDatabase

    }
   inner class DatabaseHelperNotes:SQLiteOpenHelper{
       var context:Context?=null
        constructor(context:Context):super(context,dbName,null,dbVersion){
            this.context=context
        }
        override fun onCreate(p0: SQLiteDatabase?) {
               p0!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context,"database is created",Toast.LENGTH_LONG).show()

        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
       p0!!.execSQL("DROP TABLE IF EXISTS $dbTable")

        }

    }
    fun Insert(values:ContentValues):Long{
        val ID=sqlDB!!.insert(dbTable,"",values)
         return ID

    }
    fun Query(projection: Array<String>,selection:String,selectionArgs:Array<String>,SorOrder:String):Cursor{
        val qb=SQLiteQueryBuilder()
        qb.tables=dbTable
        val cursor=qb.query(sqlDB,projection,selection,selectionArgs,null,null,SorOrder)
        return cursor

    }
    fun Delete(selection: String,selectionArgs: Array<String>):Int{
        val count=sqlDB!!.delete(dbTable,selection,selectionArgs)
        return count
    }
    fun Update(values: ContentValues,selection: String,selectionArgs: Array<String>):Int {
        val count = sqlDB!!.update(dbTable, values, selection, selectionArgs)
       return count
    }
}