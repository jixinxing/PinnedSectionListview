/*
 * Copyright (C) 2013 Sergej Shafarenka, halfbit.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package star.com.pinnedsectionlistview;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;


public class PinnedSectionListActivity extends ListActivity implements OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("aa","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeAdapter();
    }


    /**
     * 初始化adapter
     */
    private void initializeAdapter() {
        getListView().setFastScrollEnabled(false);
        setListAdapter(new SimpleAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1));
    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Item item = (Item) getListView().getAdapter().getItem(position);
        if (item != null) {
            Toast.makeText(this, "Item " + position + ": " + item.text, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Item " + position, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Item: " + v.getTag() , Toast.LENGTH_SHORT).show();
    }


    static class SimpleAdapter extends ArrayAdapter<Item> implements PinnedSectionListView.PinnedSectionListAdapter {

        private static final int[] COLORS = new int[] {
            R.color.green_light, R.color.orange_light,
            R.color.blue_light, R.color.red_light };

        public SimpleAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
            generateDataset('A', 'Z', false);
        }

        public void generateDataset(char from, char to, boolean clear) {
        	if (clear) {
                clear();
            }
            //  A--65   Z--90
            final int sectionsNumber = to - from + 1;//A-Z=sectionsNumber=26


            int sectionPosition = 0, listPosition = 0;
            for (char i=0; i<sectionsNumber; i++) {
                Item section = new Item(Item.SECTION, String.valueOf((char)('A' + i)));
                section.sectionPosition = sectionPosition;
                section.listPosition = listPosition++;
                add(section);

                final int itemsNumber = 10;
                for (int j=0;j<itemsNumber;j++) {
                    Item item = new Item(Item.ITEM, section.text.toUpperCase(Locale.ENGLISH) + " - " + j);
                    item.sectionPosition = sectionPosition;
                    item.listPosition = listPosition++;
                    add(item);
                }

                sectionPosition++;
            }
        }
        

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTextColor(Color.DKGRAY);
            view.setTag("" + position);
            Item item = getItem(position);
            if (item.type == Item.SECTION) {//大标题
                //每4个颜色一组
                view.setBackgroundColor(parent.getResources().getColor(COLORS[item.sectionPosition % COLORS.length]));
            }
            return view;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).type;
        }

        /**
         * 自定义的
         * @param viewType
         * @return
         */
        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return viewType == Item.SECTION;
        }

    }


	static class Item {

		public static final int ITEM = 0;//对应的item
		public static final int SECTION = 1;//大标题

		public final int type;
		public final String text;

		public int sectionPosition;
		public int listPosition;

		public Item(int type, String text) {
		    this.type = type;
		    this.text = text;
		}

		@Override public String toString() {
			return text;
		}
	}

}