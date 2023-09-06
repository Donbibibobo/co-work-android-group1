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
import androidx.compose.animation.core.animateDpAsState
import androidx.test.core.app.canTakeScreenshot
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
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        binding.viewModel = viewModel


        var input = binding.chatInputText.text
        val adapter = ChatAdapter()
        val chatList = mutableListOf<ChatDataClass>()

        binding.gptRecyclerView.adapter = adapter

        binding.sendButton.setOnClickListener {
            chatList.add(ChatDataClass.Sent(input.toString()))
            adapter.submitList(chatList)

            // POST sent
            adapter.notifyDataSetChanged()
            viewModel.sendToChatGpt(input.toString())
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





        viewModel.gptResponse.observe(viewLifecycleOwner, Observer {
            chatList.add(ChatDataClass.Received(it))
            chatList.add(ChatDataClass.Img("aa"))
            adapter.submitList(chatList)
            Log.i("CHAT1",chatList.toString())
            Log.i("CHAT1",it)
            adapter.notifyDataSetChanged()
        })

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