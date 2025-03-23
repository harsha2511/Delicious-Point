package com.example.adminwaveoffood

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.adapter.AddItemAdapter
import com.example.adminwaveoffood.databinding.ActivityAllItemBinding
import java.util.ArrayList

class AllItemActivity : AppCompatActivity() {
    private val binding:ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val menuFoodName = listOf("Burger","Sandwich","momo","Paneer chilly","Manchurian","Samosa","Bhelpuri","BatataPuri")
        val menuItemPrice = listOf("$2","$3","$4","$5","$2","$3","$4","$5","$1")
        val menuImage = listOf(
            R.drawable.menu1,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu4,
            R.drawable.menu5,
            R.drawable.menu1,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu4
        )

        binding.backButton.setOnClickListener{
            finish()
        }

        val adapter = AddItemAdapter(ArrayList(menuFoodName),
            ArrayList(menuItemPrice),ArrayList(menuImage))
        binding.MenuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.MenuRecyclerView.adapter=adapter
    }
}