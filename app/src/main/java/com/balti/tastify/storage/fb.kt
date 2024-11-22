package com.balti.tastify.storage

//this is utility static class for firebase

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.balti.tastify.R
import com.balti.tastify.api.Meal
import com.balti.tastify.data
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream

class fb {
    companion object {
        val TAG = "storage"
        var auth: FirebaseAuth? = null

        // user data -------------------------------------------------------------------------------
        fun getUID(): String {
            auth = FirebaseAuth.getInstance()
            val uid = auth?.currentUser?.uid
            return uid ?: ""
        }
        fun setUserInfo(user: data.User,context: Context) {
            val collectionReference = Firebase.firestore.collection("users").document(getUID())
            collectionReference.set(user)
                .addOnSuccessListener {
                    // Optionally, you can handle the success scenario here
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
        }
        fun updateUserInfo(data:Map<String,Any>,context: Context) {
            val collectionReference = Firebase.firestore.collection("users").document(getUID())
            collectionReference.update(data)
                .addOnSuccessListener {
                    // Optionally, you can handle the success scenario here
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
        }
        fun getUserInfo(context: Context,uid:String,callback: (user: data.User) -> Unit) {
            val collectionReference = Firebase.firestore.collection("users").document(uid)
            collectionReference.get().addOnSuccessListener { doc ->
                //doc is a DocumentSnapshot holds a document object
                val user = doc.toObject(data.user::class.java) //get user object
                if (user != null) {
                    callback(user)
                }else{
                    callback(data.User())  //return empty user object
                }

            }.addOnFailureListener { e ->
                callback(data.User()) //return empty user object
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
        fun realtime_getUserInfo(c:Context, uid:String = getUID(), callback: (user: data.User) -> Unit) {
            val collectionRef = Firebase.firestore.collection("users").document(uid)
            collectionRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    // Handle error
                    Log.e(TAG, "Error fetching users:", firebaseFirestoreException)
                    return@addSnapshotListener // Exit the listener if there's an error
                }

                querySnapshot?.let {
                    // Proceed if data is retrieved successfully
                    val user = it.toObject(data.user::class.java) //get user object
                    if (user != null) {
                        callback(user)
                    }else{
                        Toast.makeText(c,"Error while getting user", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


        // shopping list ---------------------------------------------------------------------------
        fun addShoppingList(data:data.ShoppingList,context: Context) {
            val collectionReference = Firebase.firestore.collection("users").document(getUID()).collection("shopping_list").document()

            //save id in the shopping list object before saving
            val id = collectionReference.id
            data.id = id

            collectionReference.set(data)
                .addOnSuccessListener {
                    // Optionally, you can handle the success scenario here
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
        }
        fun deleteShoppingList(id: String, callback: (Boolean) -> Unit) {
            val collectionReference = Firebase.firestore
                .collection("users")
                .document(getUID())
                .collection("shopping_list")
                .document(id)

            collectionReference.delete()
                .addOnSuccessListener {
                    callback(true) // Notify success
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    callback(false) // Notify failure
                }
        }
        fun getShoppingLists(callback: (user: MutableList<data.ShoppingList> ) -> Unit) {
            val collectionReference = Firebase.firestore
                .collection("users")
                .document(getUID())
                .collection("shopping_list")

            collectionReference.get()
                .addOnSuccessListener { querySnapshot ->
                    val shoppingLists = mutableListOf<data.ShoppingList>()
                    for (document in querySnapshot) {
                        val shoppingList = document.toObject(data.ShoppingList::class.java)
                        shoppingList.id = document.id // Set the document ID
                        shoppingLists.add(shoppingList)
                    }
                    // Pass the list to the callback
                    callback(shoppingLists)
                }
                .addOnFailureListener { exception ->
                    // Pass the exception to the callback
                    callback(mutableListOf( data.ShoppingList() ))
                }
        }


        // shopping list item ----------------------------------------------------------------------
        fun addShoppingListItem(
            listId: String,
            item: data.ShoppingListItem,
            callback: (Boolean) -> Unit
        ) {
            val collectionReference = Firebase.firestore
                .collection("users")
                .document(getUID())
                .collection("shopping_list")
                .document(listId)
                .collection("items")
                .document()

            val id = collectionReference.id
            item.id = id

            collectionReference.set(item)
                .addOnSuccessListener {
                    callback(true) // Notify success
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    callback(false) // Notify failure
                }
        }
        fun updateShoppingListItem(
            data:Map<String,Any>,
            context: Context,
            listId: String,
            itemId: String,
        ) {
            val collectionReference = Firebase.firestore
                .collection("users")
                .document(getUID())
                .collection("shopping_list")
                .document(listId)
                .collection("items")
                .document(itemId)

            collectionReference.update(data)
                .addOnSuccessListener {
                    // Optionally, you can handle the success scenario here
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
        }
        fun getShoppingListItems(listId: String,callback: (user: MutableList<data.ShoppingListItem> ) -> Unit) {
            val collectionReference = Firebase.firestore
                .collection("users")
                .document(getUID())
                .collection("shopping_list")
                .document(listId)
                .collection("items")

            collectionReference.get()
                .addOnSuccessListener { querySnapshot ->
                    val shoppingLists = mutableListOf<data.ShoppingListItem>()
                    for (document in querySnapshot) {
                        val item = document.toObject(data.ShoppingListItem::class.java)
                        item.id = document.id // Set the document ID
                        shoppingLists.add(item)
                    }
                    // Pass the list to the callback
                    callback(shoppingLists)
                }
                .addOnFailureListener { exception ->
                    // Pass the exception to the callback
                    callback(mutableListOf( data.ShoppingListItem() ))
                }
        }

        // saved meals -----------------------------------------------------------------------------
        fun addSaved(
            item: Meal,
            callback: (Boolean) -> Unit
        ) {
            val collectionReference = Firebase.firestore
                .collection("users")
                .document(getUID())
                .collection("saved")
                .document(item.idMeal)

            collectionReference.set(item)
                .addOnSuccessListener {
                    callback(true) // Notify success
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    callback(false) // Notify failure
                }
        }
        fun deleteSaved(id: String, callback: (Boolean) -> Unit) {
            val collectionReference = Firebase.firestore
                .collection("users")
                .document(getUID())
                .collection("saved")
                .document(id)

            collectionReference.delete()
                .addOnSuccessListener {
                    callback(true) // Notify success
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    callback(false) // Notify failure
                }
        }
        fun isSaved(mealId:String,callback: (Boolean) -> Unit) {
            val collectionReference = Firebase.firestore.collection("users").document(getUID()).collection("saved").document(mealId)
            collectionReference.get().addOnSuccessListener { doc ->
                //doc is a DocumentSnapshot holds a document object
                val saved = doc.toObject(data.SavedMeal::class.java) //get user object
                if (saved != null) {
                    callback(true)
                }else{
                    callback(false)  //return empty user object
                }

            }.addOnFailureListener { e ->
                callback(false) //return empty user object
            }
        }
        fun getAllSaved(callback: (user: MutableList<Meal> ) -> Unit) {
            val collectionReference = Firebase.firestore
                .collection("users")
                .document(getUID())
                .collection("saved")

            collectionReference.get()
                .addOnSuccessListener { querySnapshot ->
                    val list = mutableListOf<Meal>()
                    for (document in querySnapshot) {
                        val item = document.toObject(Meal::class.java)
                        item.idMeal = document.id // Set the document ID
                        list.add(item)
                    }
                    // Pass the list to the callback
                    callback(list)
                }
                .addOnFailureListener { exception ->
                    // Pass the exception to the callback
                    callback(mutableListOf())
                }
        }

        // images ----------------------------------------------------------------------------------
        fun update_profile_image(c: Context, uri: Uri?, callback: (Boolean) -> Unit) {
            // create an instance from the database
            val storage = Firebase.storage
            auth = FirebaseAuth.getInstance()
            val uid = auth?.currentUser?.uid
            //file path
            val ref = storage.reference.child("users").child(uid.toString()).child("profileImage")

            if (uri != null) {
                ref.putFile(uri).addOnSuccessListener {
                    Toast.makeText(c, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                    callback(true)
                }.addOnFailureListener {
                    callback(false)
                    Toast.makeText(c, it.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "update_profile_image: ", it)
                }
            }
        }

        fun update_profile_image(c: Context, bitmap: Bitmap?, callback: (Boolean) -> Unit) {
            // Create an instance of the database
            val storage = Firebase.storage
            val auth = FirebaseAuth.getInstance()
            val uid = auth.currentUser?.uid
            // File path
            val ref = storage.reference.child("users").child(uid.toString()).child("profileImage")

            if (bitmap != null) {
                // Convert Bitmap to ByteArray
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                // Upload the ByteArray to Firebase Storage
                ref.putBytes(data).addOnSuccessListener {
                    Toast.makeText(c, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                    callback(true)
                }.addOnFailureListener {
                    callback(false)
                    Toast.makeText(c, it.message, Toast.LENGTH_SHORT).show()
                    Log.e("TAG", "update_profile_image: ", it)
                }
            } else {
                callback(false)
                Toast.makeText(c, "Bitmap is null", Toast.LENGTH_SHORT).show()
            }
        }
        fun load_profile_image(context: Context, userID: String, imageView: ShapeableImageView) {
            // create an instance from the database
            val storage = Firebase.storage
            auth = FirebaseAuth.getInstance()
            //file path
            val ref = storage.reference.child("users").child(userID).child("profileImage")

            ref.downloadUrl.addOnSuccessListener {
                //this returns an url for the file
                //Picasso.get().load(it).into(imageView)
                try {
                    Glide.with(context)
                        .load(it)
                        .placeholder(R.color.colorCard) // Replace with your placeholder drawable
                        .error(R.color.colorCard) // Fallback if the image fails to load
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image efficiently
                        .into(imageView)
                } catch (e: Exception) {
                    Log.e(TAG, "load_profile_image: ${e.message}", e)
                }


            }.addOnFailureListener {
                imageView.setImageResource(R.color.colorCard)
                Log.e(TAG, "load_profile_image: ${it.message}")
            }
        }

        fun load_profile_image(context: Context, userID: String, imageView: ImageView) {
            // create an instance from the database
            val storage = Firebase.storage
            auth = FirebaseAuth.getInstance()
            //file path
            val ref = storage.reference.child("users").child(userID).child("profileImage")

            ref.downloadUrl.addOnSuccessListener {
                //this returns an url for the file
                //Picasso.get().load(it).into(imageView)
                try {
                    Glide.with(context)
                        .load(it)
                        .placeholder(R.color.colorCard)
                        .error(R.color.colorCard)
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image efficiently
                        .into(imageView)
                } catch (e: Exception) {
                    Log.e(TAG, "load_profile_image: ${e.message}")
                }

            }.addOnFailureListener {
                imageView.setImageResource(R.color.colorCard)
                Log.e(TAG, "load_profile_image: ${it.message}")
            }
        }

        //------------------------------------------------------------------------------------------
        fun load_image_to_view(context: Context,img:Uri? ,imageView: ShapeableImageView) {
            img.let {
                try {
                    Glide.with(context)
                        .load(it)
                        .placeholder(R.color.colorCard)
                        .error(R.color.colorCard)
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image efficiently
                        .into(imageView)
                } catch (e: Exception) {
                    Log.e(TAG, "load_profile_image: ${e.message}")
                }
            }
        }
        fun load_image_to_view(context: Context,img:Uri? ,imageView: ImageView) {
            img.let {
                try {
                    Glide.with(context)
                        .load(it)
                        .placeholder(R.color.colorCard)
                        .error(R.color.colorCard)
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image efficiently
                        .into(imageView)
                } catch (e: Exception) {
                    Log.e(TAG, "load_profile_image: ${e.message}")
                }
            }
        }
        //------------------------------------------------------------------------------------------
        fun logout() {
            FirebaseAuth.getInstance().signOut()
        }
    }
}