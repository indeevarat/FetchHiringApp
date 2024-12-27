package com.example.fetchhiringapp
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchhiringapp.Item
import com.example.fetchhiringapp.R
import com.example.fetchhiringapp.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: GroupedItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        GlobalScope.launch(Dispatchers.Main) {
            val items = RetrofitInstance.api.getItems()
            val groupedItems = processAndGroupData(items)
            itemAdapter = GroupedItemAdapter(groupedItems)
            recyclerView.adapter = itemAdapter
        }
    }

    private fun processAndGroupData(items: List<Item>): List<Any> {
        val filteredItems = items
            .filter { !it.name.isNullOrBlank() }
            .sortedWith(compareBy({ it.listId }, { it.name }))

        val groupedList = mutableListOf<Any>()
        val groupedItems = filteredItems.groupBy { it.listId }

        groupedItems.forEach { (listId, itemList) ->
            groupedList.add("List ID: $listId") // Add a header for each group
            groupedList.addAll(itemList)       // Add items under the header
        }

        return groupedList
    }
}
