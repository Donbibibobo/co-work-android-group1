package app.appworks.school.stylish.chat

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.stylish.MainActivity
import app.appworks.school.stylish.databinding.FragmentChatBinding
import app.appworks.school.stylish.util.RealPathUtil
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.security.cert.CertPath

class ChatFragment : Fragment() {

    private lateinit var viewModel: ChatViewModel
    private lateinit var path: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChatBinding.inflate(inflater)

        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        val input = listOf(binding.gptInputText.text.toString())
        val adapter = ChatAdapter()

        val chatList = mutableListOf<ChatDataClass>()

        val sen1 = ChatDataClass.Sent("Who are you?")
        val received1 = ChatDataClass.Received("I'm chat!")

        chatList.add(sen1)
        chatList.add(received1)

        binding.gptRecyclerView.adapter = adapter

        binding.sendButton.setOnClickListener {
            adapter.submitList(chatList)

            // POST sent
        }

        binding.imageButton.setOnClickListener{
            // select Image
            if (ContextCompat.checkSelfPermission(this.requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                val intent = Intent()
                intent.setType("image/*")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(intent, 10)
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
            }
        }


        viewModel.bitmap.observe(viewLifecycleOwner, Observer {
            binding.testImage.setImageBitmap(it)
        })

        binding.sendButton2.setOnClickListener{
            // API vuttom
            viewModel.addImage()
        }






















        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 10 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            val context: Context = requireActivity()
            path = RealPathUtil.getRealPath(context, uri!!).toString()
            Log.i("ttrryy", "path: $path")

            viewModel.bitmap.value = BitmapFactory.decodeFile(path)
            Log.i("ttrryy", "bitmap: $viewModel.bitmap.valu")

            viewModel.path = path

        }
    }



}