package Mene.example.notesapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*
import android.widget.EditText
import com.example.notesapp.R


class MainActivity : AppCompatActivity() { // AppCompatActivity makes sure your app has action bar
    var listNotes=ArrayList<note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this,"onCreate",Toast.LENGTH_LONG).show()


        LoadQuery("%")


    }

    override  fun onResume() {
        super.onResume()
        LoadQuery("%")
        Toast.makeText(this,"onResume",Toast.LENGTH_LONG).show()
    }
    /*

    override fun onStart() {
        super.onStart()
        Toast.makeText(this,"onStart",Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this,"onPause",Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(this,"onStop",Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"onDestroy",Toast.LENGTH_LONG).show()
    }

    override fun onRestart() {
        super.onRestart()
        Toast.makeText(this,"onRestart",Toast.LENGTH_LONG).show()
    }
    */

    fun LoadQuery(title:String){
        // load from DB
        var dbManager= DbManager(this)
        val projection= arrayOf("ID","Title","Description")
        val selectionArgs= arrayOf(title)
        val cursor=dbManager.Query(projection,"Title like ?",selectionArgs,"Title")
        listNotes.clear()
        if(cursor.moveToFirst()){
            do{
                val ID=cursor.getInt(cursor.getColumnIndex("ID"))
                val Title=cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(note(ID, Title, Description))
            }while(cursor.moveToNext())
        }
        //  var myNotesAdapter=MyNotesAdapter(this,listNotes)
        var myNotesAdapter= MyNotesAdapter(this,listNotes)
        lvNotes.adapter=myNotesAdapter


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val sv: SearchView = menu.findItem(R.id.search).actionView as SearchView //retrieve items with findItem()
        // var sv : SearchView = menu.findItem(R.id.search).actionView as SearchView
        var sm=getSystemService(Context.SEARCH_SERVICE)as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext,query,Toast.LENGTH_LONG).show()
                //TODO search database
                LoadQuery("%$query%")
                //LoadQuery("%"+query+"% ")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!=null) {
            when (item.itemId) {
                R.id.addNote -> {
                    //go to add page
                    var intent=Intent(this, AddNotes::class.java)
                    startActivity(intent)


                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    inner class MyNotesAdapter:BaseAdapter{

        // Adapter->The Adapter is also responsible for making a View for each item in the data set.
        var listNotesAdapter=ArrayList<note>()
        var context:Context?=null
        constructor(context:Context,listNotesAdapter:ArrayList<note>):super(){
            this.listNotesAdapter=listNotesAdapter
            this.context=context
        }

 //getView-> Get a View that displays the data at the specified position in the data set.
        //viewGroup-> A ViewGroup is a special view that can contain other views (called children.) The view group is the base class for layouts and views containers.
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            var myView=layoutInflater.inflate(R.layout.ticket,null)
            var myNote=listNotesAdapter[p0]
            myView.tvTitle.text=myNote.noteName
            myView.tvDes.text=myNote.noteDes
            myView.ivDelete.setOnClickListener {
                var dbManager= DbManager(this.context!!)
                var selectionArgs=arrayOf(myNote.noteID.toString())
                dbManager.Delete("ID=?",selectionArgs)
                LoadQuery("%")
            }
            myView.ivUpdate.setOnClickListener{
                GoToUpdate(myNote)
            }
            return myView

        }

        override fun getItem(p0: Int): Any {
            return listNotesAdapter[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }




    }
    fun GoToUpdate(note: note){
        var intent=Intent(this, AddNotes::class.java)
        intent.putExtra("ID",note.noteID)
        intent.putExtra("name",note.noteName)
        intent.putExtra("des",note.noteDes)
        startActivity(intent)
    }


}
