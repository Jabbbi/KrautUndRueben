package net.KrautUndRueben.krautundrueben

import android.app.SearchManager
import android.widget.TextView

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.cursoradapter.widget.CursorAdapter
import com.google.gson.Gson
import net.KrautUndRueben.krautundrueben.HTTPClient.MarketPlace
import net.KrautUndRueben.krautundrueben.R


class SuggestionCursorAdapter(context: Context, c: Cursor) : CursorAdapter(context, c, 0) {


    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val text1 = view.findViewById(R.id.txt_search_suggestion_item) as TextView

        val text1String =
            cursor.getString(cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1))

        val jsonText = view.findViewById(R.id.txt_json) as TextView

        val gson = Gson()
        text1.setText(gson.fromJson<MarketPlace>(text1String, MarketPlace::class.java).name)
        jsonText.setText(text1String)
        view.setBackgroundColor(Color.WHITE)

        view.setOnClickListener {

            val i = Intent(context, MarketPlaceInfo::class.java)
            val b = Bundle()
            b.putString("json", it.findViewById<TextView>(R.id.txt_json).text.toString())
            i.putExtras(b)
            context.startActivity(i)

        }
    }
}
