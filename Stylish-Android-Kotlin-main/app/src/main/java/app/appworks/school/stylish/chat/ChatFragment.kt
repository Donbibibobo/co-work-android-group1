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
import androidx.core.widget.doAfterTextChanged
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

        binding.chatInputText.doAfterTextChanged {
            if(it.toString() == ""){
                Log.i("chatboxtest", "doAfter called")
                binding.sendButton.isEnabled = false
                binding.sendButton.alpha = 0.6f
            } else {
                binding.sendButton.isEnabled = true
                binding.sendButton.alpha = 1f
            }
        }

        binding.sendButton.setOnClickListener {
            binding.loadingMessage.visibility = View.VISIBLE

            binding.chatInputText.setText("")
            chatList.add(ChatDataClass.Sent(input.toString()))
            adapter.submitList(chatList)
            adapter.notifyDataSetChanged()
            viewModel.sendToChatGpt(input.toString())

        }

        // select Image

        binding.imageButton.setOnClickListener{
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
            binding.loadingMessage.visibility = View.VISIBLE

            // bind
            chatList.add(ChatDataClass.Img(it))
            adapter.submitList(chatList)
            adapter.notifyDataSetChanged()

        })


        viewModel.path.observe(viewLifecycleOwner, Observer {
            // POST img
            viewModel.addImage(it)
        })



        viewModel.chatImgResponse.observe(viewLifecycleOwner, Observer {
            binding.loadingMessage.visibility = View.GONE

            chatList.add(ChatDataClass.Received(it!!))
            adapter.submitList(chatList)
            adapter.notifyDataSetChanged()


        })




        viewModel.gptResponse.observe(viewLifecycleOwner, Observer {
            binding.loadingMessage.visibility = View.GONE

            chatList.add(ChatDataClass.Received(it))
            adapter.submitList(chatList)
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

            viewModel.path.value = path

        }
    }



}