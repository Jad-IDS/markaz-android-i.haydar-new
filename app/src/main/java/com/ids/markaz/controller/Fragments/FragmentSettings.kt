package com.ids.markaz.controller.Fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ids.inpoint.utils.RetrofitClient
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.markaz.R
import com.ids.markaz.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.markaz.controller.MyApplication
import com.ids.markaz.model.ResponseInvestmentInfo
import com.ids.markaz.model.ResponseUserImage
import com.ids.markaz.utils.AppConstants
import com.ids.markaz.utils.AppHelper
import com.ids.markaz.utils.AppHelper.Companion.setRoundImage
import com.ids.markaz.utils.RetrofitInterface
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class FragmentSettings : Fragment() , RVOnItemClickListener {

    var imageName=""
    var ImageBase64=""
    var isImagePicked=false
    var selectedImage=""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(com.ids.markaz.R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        init()
    }


    override fun onItemClicked(view: View, position: Int) {

    }


    fun init(){

        if(MyApplication.languageCode==AppConstants.LANG_ENGLISH) {
            tvName.text = MyApplication.selectedUserClient!!.nameEn
        }
        else{
            tvName.text = MyApplication.selectedUserClient!!.nameAr
        }

        ivUser.setOnClickListener{
            val permission = ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)
            if(permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    20
                )
            }else{
                changeImage()
            }
        }

        getProfileImage()
    }





    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 20) {

            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                changeImage()
            }else
                Toast.makeText(activity,getString(R.string.plz_allow_access),Toast.LENGTH_LONG).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1010) {
            if (data == null) {
                Toast.makeText(activity, getString(R.string.unable_pick), Toast.LENGTH_LONG).show()
                return
            }
            val selectedImageUri = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = activity!!.getContentResolver().query(selectedImageUri!!, filePathColumn, null, null, null)

            if (cursor != null) {

                try {
                    cursor.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    selectedImage = cursor.getString(columnIndex)
                    imageName = File(selectedImage).name



                    ImageBase64 = resizeBase64Image(encoder(selectedImage))
                    isImagePicked = true


                    try {
                        setRoundImage(activity!!, ivUser, selectedImage, true)
                    } catch (e2: Exception) {
                    }
                    //updateProfileImage()

                    cursor.close()
                }catch (e:Exception){      Toast.makeText(activity, getString(R.string.unable_to_load), Toast.LENGTH_LONG).show()}
            } else {
                Toast.makeText(activity, getString(R.string.unable_to_load), Toast.LENGTH_LONG).show()
            }
        }
    }





    private fun changeImage(){
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_PICK
        val chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.choose_image))
        startActivityForResult(chooserIntent, 1010)
    }



    private fun getProfileImage(){
        var date = SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(Date())

        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getProfileImage(
                MyApplication.clientId


            )?.enqueue(object : Callback<ResponseUserImage> {
                override fun onResponse(call: Call<ResponseUserImage>, response: Response<ResponseUserImage>) {
                    try{
                        if(response.code()==401)
                            AppHelper.reLogin(activity!!)}catch (e:Exception){}
                    try{
                        onUserProfileRetreived(response)
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<ResponseUserImage>, throwable: Throwable) {
                }
            })

    }


/*    private fun updateProfileImage(){
        loading.visibility = View.VISIBLE

        if(ImageBase64.isEmpty()){
            try{ImageBase64=encoderURL()
                val tsLong = System.currentTimeMillis() / 1000
                val ts = tsLong.toString()
                imageName="img"+ts.toString()+".jpg"

            }catch (e: Exception){
                Log.wtf("imageException1",e.toString())
            }

        }
        Log.wtf("imagedecoded",ImageBase64);
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.updateProfileImage(
                MyApplication.clientId,
                ImageBase64.trim()
           )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    try{
                        loading.visibility=View.GONE
                    }catch (E: java.lang.Exception){
                    }
                }
                override fun onFailure(call: Call<Boolean>, throwable: Throwable) {
                }
            })

    }*/



    private fun onUserProfileRetreived(response:Response<ResponseUserImage>){
        loading.visibility=View.GONE
        AppHelper.userProfile=response.body()
        setInfo()


    }

    fun encoder(filePath: String): String{

        val bm = BitmapFactory.decodeFile(filePath)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) //bm is the bitmap object
        val b = baos.toByteArray()
        val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
        return encodedImage
    }


    fun encoderURL(): String{

        val drawable = ivUser.drawable as RoundedBitmapDrawable
        val bm = drawable.bitmap
        val baos = ByteArrayOutputStream()
        bm!!.compress(Bitmap.CompressFormat.JPEG, 100, baos) //bm is the bitmap object
        val b = baos.toByteArray()
        val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
        return encodedImage
    }


    fun resizeBase64Image(base64image: String): String {
        val encodeByte = Base64.decode(base64image.toByteArray(), Base64.DEFAULT)
        val options = BitmapFactory.Options()
        options.inPurgeable = true
        var image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size, options)


        if (image.height <= 400 && image.width <= 400) {
            return base64image
        }
        image = Bitmap.createScaledBitmap(image, 150, 150, false)

        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)

        val b = baos.toByteArray()
        System.gc()
        return Base64.encodeToString(b, Base64.NO_WRAP)

    }



    @Throws(FileNotFoundException::class)
    fun decodeUri(c: Context, uri: Uri, requiredSize: Int): Bitmap? {
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        BitmapFactory.decodeStream(c.contentResolver.openInputStream(uri), null, o)

        var width_tmp = o.outWidth
        var height_tmp = o.outHeight
        var scale = 1

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break
            width_tmp /= 2
            height_tmp /= 2
            scale *= 2
        }

        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        return BitmapFactory.decodeStream(c.contentResolver.openInputStream(uri), null, o2)
    }


    private fun setInfo(){
        val userProfile = AppHelper.userProfile
        imageName= userProfile!!.image.toString()
        if(!imageName.isEmpty()) {
            try {
                setRoundImage(activity!!, ivUser, imageName, false)
            } catch (e2: Exception) {
            }


        }

    }
}