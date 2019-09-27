package net.KrautUndRueben.krautundrueben

import android.app.SearchManager
import android.widget.TextView

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.view.*
import androidx.cursoradapter.widget.CursorAdapter
import net.KrautUndRueben.krautundrueben.R

class SuggestionCursorAdapter(context: Context, c: Cursor) : CursorAdapter(context, c, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val text1 = view.findViewById(R.id.txt_search_suggestion_item) as TextView

        val text1String =
            cursor.getString(cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1))

        text1.setText(text1String)
        view.setBackgroundColor(Color.WHITE)
    }
}