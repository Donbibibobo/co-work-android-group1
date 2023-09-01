package app.appworks.school.stylish.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.R
import app.appworks.school.stylish.StylishApplication
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.databinding.ItemPaymentFormBinding
import app.appworks.school.stylish.databinding.ItemPaymentProductBinding

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class PaymentAdapter(val viewModel: PaymentViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // the data of adapter
    var products: List<Product>? = null

    override fun getItemCount(): Int {
        products?.let {
            return when (it.size) {
                0 -> 0
                else -> it.size + 1
            }
        }
        return 0
    }

    class ProductViewHolder(private var binding: ItemPaymentProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product?) {

            product?.let {
                binding.product = it
                binding.executePendingBindings()
            }
        }
    }

    /**
     * Implements [LifecycleOwner] to support Data Binding
     */
    class FormViewHolder(var binding: ItemPaymentFormBinding) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

        init {
            binding.spinnerPaymentMethods.adapter = PaymentSpinnerAdapter(
                StylishApplication.instance.resources.getStringArray(R.array.payment_method_list)
            )
        }

        fun bind() {

            binding.lifecycleOwner = this
            binding.viewModel?.setupTpd(binding.formPaymentTpd)
            binding.executePendingBindings()
        }

        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }

        fun onAttach() {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        fun onDetach() {
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        }

        override val lifecycle: Lifecycle
            get() = lifecycleRegistry
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_PRODUCT -> ProductViewHolder(
                ItemPaymentProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            ITEM_VIEW_TYPE_FORM -> FormViewHolder(
                ItemPaymentFormBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
                    viewModel = this@PaymentAdapter.viewModel
                }
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ProductViewHolder -> {
                holder.bind(products?.get(position))
            }
            is FormViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            products?.size ?: 0 -> ITEM_VIEW_TYPE_FORM
            else -> ITEM_VIEW_TYPE_PRODUCT
        }
    }

    /**
     * Submit data list and refresh adapter by [notifyDataSetChanged]
     * @param products: [List] [Product]
     */
    fun submitProducts(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }

    /**
     * It for [LifecycleRegistry] change [onViewAttachedToWindow]
     */
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        when (holder) {
            is FormViewHolder -> holder.onAttach()
        }
    }

    /**
     * It for [LifecycleRegistry] change [onViewDetachedFromWindow]
     */
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        when (holder) {
            is FormViewHolder -> holder.onDetach()
        }
    }

    companion object {
        private const val ITEM_VIEW_TYPE_FORM = 0x00
        private const val ITEM_VIEW_TYPE_PRODUCT = 0x01
    }
}
