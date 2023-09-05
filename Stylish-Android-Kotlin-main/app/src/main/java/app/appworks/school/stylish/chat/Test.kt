package app.appworks.school.stylish.chat
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import app.appworks.school.stylish.R
//
//data class ChatMessage(val message: String)
//
//class ChaatFragment : Fragment() {
//
//    private lateinit var chatAdapter: ChatAdapter
//    private val chatMessages = mutableListOf<ChatMessage>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_chat, container, false)
//
//        // 初始化 RecyclerView 和适配器
////        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
////        chatAdapter = ChaatAdapter(chatMessages)
////        recyclerView.adapter = chatAdapter
//
//        // 在这里添加用户输入的文本到 RecyclerView
////        val sendButton: Button = view.findViewById(R.id.sendButton)
//        val inputEditText: EditText = view.findViewById(R.id.inputEditText)
//
//        sendButton.setOnClickListener {
//            val userInput = inputEditText.text.toString()
//            if (userInput.isNotEmpty()) {
//                val chatMessage = ChatMessage(userInput)
//                chatAdapter.addMessage(chatMessage)
//                inputEditText.text.clear()
//            }
//        }
//        return view
//    }
//}
//
//class ChaatAdapter(private val messages: MutableList<ChatMessage>) :
//    RecyclerView.Adapter<ChatAdapter.ChaatViewHolder>() {
//
//    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_message, parent, false)
//        return ChatViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
//        val message = messages[position]
//        holder.messageTextView.text = message.message
//    }
//
//    override fun getItemCount(): Int {
//        return messages.size
//    }
//
//    fun addMessage(message: ChatMessage) {
//        messages.add(message)
//        notifyItemInserted(messages.size - 1)
//    }
//}