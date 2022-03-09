import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.firebasecontact.R
import com.example.firebasecontact.Show
import com.example.firebasecontact.Users
import com.google.firebase.database.FirebaseDatabase

class Adapter(val mCtx: Context, val layoutResId: Int, val list: List<Users> )
    : ArrayAdapter<Users>(mCtx,layoutResId,list){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId,null)

        val textNama = view.findViewById<TextView>(R.id.textName)
        val textStatus = view.findViewById<TextView>(R.id.textDetail)
        val update = view.findViewById<TextView>(R.id.update)
        val delete = view.findViewById<TextView>(R.id.delete)

        val user = list[position]

        textNama.text = user.name
        textStatus.text = user.detail

       
        update.setOnClickListener {
            showUpdateDialog(user)
        }
        delete.setOnClickListener {
            Deleteinfo(user)
        }

        return view

    }

    private fun showUpdateDialog(user: Users) {
        val builder = AlertDialog.Builder(mCtx)

        builder.setTitle("Update")

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.update, null)

        val textName = view.findViewById<EditText>(R.id.Name)
        val textDetail = view.findViewById<EditText>(R.id.Detail)

        textName.setText(user.name)
        textDetail.setText(user.detail)

        builder.setView(view)

        builder.setPositiveButton("Update") { dialog, which ->

            val dbUsers = FirebaseDatabase.getInstance().getReference("USERS")

            val nama = textName.text.toString().trim()

            val detail = textDetail.text.toString().trim()

            if (nama.isEmpty()){
                textName.error = "please enter name"
                textName.requestFocus()
                return@setPositiveButton
            }

            if (detail.isEmpty()){
                textDetail.error = "please enter status"
                textDetail.requestFocus()
                return@setPositiveButton
            }

            val user = Users(user.id,nama,detail)

            dbUsers.child(user.id).setValue(user).addOnCompleteListener {
                Toast.makeText(mCtx,"Updated",Toast.LENGTH_SHORT).show()
            }

        }

        builder.setNegativeButton("No") { dialog, which ->

        }

        val alert = builder.create()
        alert.show()
    }


        private fun Deleteinfo(user: Users) {
            val progressDialog = ProgressDialog(context, R.style.Theme_MaterialComponents_Light_Dialog)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Deleting...")
            progressDialog.show()
            val mydatabase = FirebaseDatabase.getInstance().getReference("USERS")
            mydatabase.child(user.id).removeValue()
            Toast.makeText(mCtx,"Deleted!!",Toast.LENGTH_SHORT).show()
            val intent = Intent(context, Show::class.java)
            context.startActivity(intent) }
    }

