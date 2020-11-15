package com.heigvd.sym_labo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import com.heigvd.sym_labo2.comm.CommunicationEventListener
import com.heigvd.sym_labo2.comm.SymComManager
import com.heigvd.sym_labo2.models.Author
import com.heigvd.sym_labo2.models.Post
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.collections.ArrayList

class Activity5 : AppCompatActivity() {

    private lateinit var listView : ListView
    private lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_5)

        listView = findViewById(R.id.author_list_view)
        progressBar = findViewById(R.id.progress_bar)
        progressBar.visibility = View.VISIBLE

        val names = ArrayList<String>()
        val authors = ArrayList<Author>()

        val adapter = ArrayAdapter(baseContext, android.R.layout.simple_list_item_1, names)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val element = authors.get(position)
            progressBar.visibility = View.VISIBLE

            // Display author information when we receive the server's response
            val mcm_author = SymComManager(object : CommunicationEventListener {
                override fun handleServerResponse(response: String) {
                    runOnUiThread {
                        val posts = deserializeToAuthorPostsListFromJson(response)
                        val titles = ArrayList<String>()
                        for(post in posts) {
                            titles.add(post.title.toUpperCase(Locale.ROOT) + "\n" + post.description)
                        }
                        progressBar.visibility = View.GONE
                        adapter.clear()
                        adapter.addAll(titles)
                        adapter.notifyDataSetChanged();
                    }
                }
            })

            val authorPostsRequest = "{ \"query\": \"{ allPostByAuthor(authorId: ${element.id}){id, title, description }}\"}"
            mcm_author.sendRequest(MainActivity.LAB_SERVER + "api/graphql", authorPostsRequest, "application/json")
        }

        val mcm = SymComManager(object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {
                runOnUiThread {
                    authors.addAll(deserializeToAuthorListFromJson(response))
                    names.addAll(authors.map { it.last_name + " " + it.first_name })
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                }
            }
        })

        val allAuthorsRequest = "{ \"query\": \"{ allAuthors{id, last_name, first_name }}\"}"
        mcm.sendRequest(MainActivity.LAB_SERVER + "api/graphql", allAuthorsRequest, "application/json")
    }

    fun deserializeToAuthorListFromJson(jsonContent: String) : List<Author> {
        // Gettng only the content we need
        val jsonAuthorsContent = jsonContent.substring(jsonContent.indexOf('['), jsonContent.indexOf(']') + 1)
        // ignoring unknown keys such as "infos":"..."
        return Json{ ignoreUnknownKeys = true }.decodeFromString(ListSerializer(Author.serializer()), jsonAuthorsContent);
    }

    fun deserializeToAuthorPostsListFromJson(jsonContent: String) : List<Post> {
        // Gettng only the content we need
        val jsonAuthorsContent = jsonContent.substring(jsonContent.indexOf('['), jsonContent.indexOf(']') + 1)
        // ignoring unknown keys such as "infos":"..."
        return Json{ ignoreUnknownKeys = true }.decodeFromString(ListSerializer(Post.serializer()), jsonAuthorsContent);
    }

    companion object {
        private const val TAG: String = "Activity5"
    }
}