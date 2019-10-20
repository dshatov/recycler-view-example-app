package io.fumy.recyclerview.exampleapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {
    static char[] ALPHA1 = "AOEIU".toCharArray();
    static char[] ALPHA2 = "BCDFGHJKLMNPRSTVWXYZ".toCharArray();

    static List<Category> getRandomData() {
        final Random random = new Random();
        return new ArrayList<Category>() {{
            for (int i = 0; i < 100; ++i) {
                add(new Category(
                        getRandomName(random, 12),
                        new ArrayList<Subcategory>() {{
                            for (int i = 0; i < 100; ++i) {
                                add(new Subcategory(
                                        getRandomName(random, 20),
                                        random.nextInt(95)
                                ));
                            }
                        }}
                ));
            }
        }};
    }

    static String getRandomName(Random random, int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i += 2) {
            chars[i] = ALPHA2[random.nextInt(ALPHA1.length)];
            chars[i + 1] = ALPHA1[random.nextInt(ALPHA1.length)];
        }
        return new String(chars);
    }

    //----------------------------------------------------------------------------------------------

    RecyclerView categoriesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Category> categories = getRandomData();

        categoriesView = findViewById(R.id.categories_list);
        categoriesView.setHasFixedSize(true);
        categoriesView.setLayoutManager(new LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
        ));
        categoriesView.setAdapter(new CategoriesAdapter(categories));
    }

    //----------------------------------------------------------------------------------------------

    static class Category {
        String name;
        List<Subcategory> subcategories;

        Category(String name, List<Subcategory> subcategories) {
            this.name = name;
            this.subcategories = subcategories;
        }
    }

    static class Subcategory {
        String name;
        int progress;

        Subcategory(String name, int progress) {
            this.name = name;
            this.progress = progress;
        }
    }

    //----------------------------------------------------------------------------------------------

    static class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        List<Category> data;

        CategoriesAdapter(List<Category> data) {
            this.data = data;
        }


        @Override
        public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CategoriesAdapter.ViewHolder holder, int position) {
            Category category = data.get(position);
            holder.categoryName.setText(category.name);
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    holder.subcategoriesList.getContext(),
                    RecyclerView.HORIZONTAL,
                    false
            );
            holder.subcategoriesList.setLayoutManager(layoutManager);
            holder.subcategoriesList.setAdapter(new SubcategoriesAdapter(category.subcategories));
            holder.subcategoriesList.setRecycledViewPool(viewPool);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            RecyclerView subcategoriesList;
            TextView categoryName;

            ViewHolder(View itemView) {
                super(itemView);
                categoryName = itemView.findViewById(R.id.category_name);
                subcategoriesList = itemView.findViewById(R.id.subcategories_list);
            }
        }
    }

    //----------------------------------------------------------------------------------------------

    static class SubcategoriesAdapter extends RecyclerView.Adapter<SubcategoriesAdapter.ViewHolder> {

        List<Subcategory> data;

        SubcategoriesAdapter(List<Subcategory> data) {
            this.data = data;
        }


        @Override
        public SubcategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subcategory_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Subcategory subcategory = data.get(position);
            holder.subcategoryName.setText(subcategory.name);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView subcategoryName;
            ProgressBar subcategoryProgress;
            ImageView subcategoryImage;

            ViewHolder(View itemView) {
                super(itemView);
                subcategoryName = itemView.findViewById(R.id.subcategory_name);
                subcategoryProgress = itemView.findViewById(R.id.subcategory_progress);
                subcategoryImage = itemView.findViewById(R.id.subcategory_image);
            }
        }
    }
}
