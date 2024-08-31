package com.company.transmitter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class PageFragment extends Fragment {
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    Item[] cakes = new Item[] {new Item(R.drawable.cake_1, "Chocolate Delight", 45, false, 0, R.drawable.big_1, "This is a classic chocolate cake, which consists of several layers of chocolate sponge cake, soaked in chocolate cream and decorated with chocolate glaze. The cake has a rich chocolate taste and delicate texture. It's perfect for chocolate lovers."),
            new Item(R.drawable.cake_2, "Fruit Paradise", 55, false, 1, R.drawable.big_2, "This cake is a combination of a delicate sponge cake with fruit filling and butter cream. The filling uses fresh or canned fruits such as strawberries, raspberries, peaches or pineapples. Fruits give the cake a fresh and bright taste, and the cream makes it soft and airy."),
            new Item(R.drawable.cake_3, "Honeymoon", 60, false, 2, R.drawable.big_3, "“Honeymoon” cake is a combination of honey cakes with delicate sour cream. The cakes have a rich honey taste and aroma, and the cream gives the cake a slight sourness. This cake is perfect for a romantic dinner or celebration."),
            new Item(R.drawable.cake_4, "Red Velvet", 52, false, 3, R.drawable.big_4, "Red velvet is a popular American cake consisting of a moist and spongy red cake layer that contrasts with a snow-white cream cheese frosting. The taste of the cake is very delicate: it has berry notes, soft velvety, and pleasant sweetness.")};

    Item[] cupcakes = new Item[] {new Item(R.drawable.capcake_1, "Vanilla cupcakes with chocolate frosting", 38, false, 4, R.drawable.big_5, "These cupcakes have a creamy vanilla base and are topped with rich chocolate frosting. They are ideal for lovers of classic taste."),
            new Item(R.drawable.capcake_2, "Red Velvet Cupcakes", 35, false, 5, R.drawable.big_6, "Red velvet cupcakes have a rich red color and a delicate flavor. Their base consists of a mixture of butter, sugar, eggs and flour, as well as a small amount of cocoa powder and food coloring. These cupcakes are decorated with buttercream and sprinkled with chocolate chips."),
            new Item(R.drawable.capcake_3, "Carrot cupcakes", 34, false, 6, R.drawable.big_7, "Carrot cupcakes are a great choice for those who love unusual flavors. They are based on carrots, which gives them a bright orange color and a sweetish taste. These cupcakes are usually decorated with butter or cheese frosting and may be topped with nuts or candied fruits."),
            new Item(R.drawable.capcake_4, "Lemon cupcakes", 38, false, 7, R.drawable.big_8, "Lemon cupcakes have a refreshing citrus flavor. Their base is made from butter, sugar and eggs, and then lemon juice and zest are added. These cupcakes can be decorated with lemon glaze or sprinkled with powdered sugar.")};

    Item[] cheesecakes = new Item[] {new Item(R.drawable.cheesecake_1, "Classic cheesecake", 60, false, 12, R.drawable.big_13, "This is the most popular type of cheesecake, which consists of a shortcrust pastry base and cream cheese filling. The filling has a creamy texture and delicate taste. The cheesecake is baked in the oven until golden brown."),
            new Item(R.drawable.cheesecake_2, "New York cheesecake", 65, false, 13, R.drawable.big_14, "This type of cheesecake differs from the classic one in that it has a denser and richer filling. The filling includes cream cheese, eggs and sugar. Cheesecake is also baked in the oven, but longer than the classic one."),
            new Item(R.drawable.cheesecake_3, "Chocolate cheesecake", 62, false, 14, R.drawable.big_15, "Chocolate is added to this type of cheesecake, which gives it a rich taste and aroma. Chocolate can be added to the shortcrust pastry base or cream cheese filling. The cheesecake can be decorated with chocolate glaze or sprinkled with chocolate chips."),
            new Item(R.drawable.cheesecake_4, "Caramel cheesecake", 63, false, 15, R.drawable.big_16, "Cheesecake with caramel taste and aroma. To prepare it, caramel is used, which is added to the cream cheese filling or as a topping. The cheesecake is very sweet and rich.")};

    Item[] muffins = new Item[] {new Item(R.drawable.muffin_1, "Chocolate muffins with cherries", 33, false, 8, R.drawable.big_9, "These muffins have a rich chocolate taste and aroma, as well as a delicate texture. They are perfect for chocolate lovers. Cherries are used as a filling, which gives the muffins a sweet and sour taste."),
            new Item(R.drawable.muffin_2, "Banana and chocolate muffins", 35, false, 9, R.drawable.big_10, "Muffins filled with banana and chocolate are a combination of delicate banana flavor and rich chocolate flavor. The banana adds sweetness and moisture to the muffin, while the chocolate adds aroma and flavor. These muffins are perfect for breakfast or snack."),
            new Item(R.drawable.muffin_3, "Lemon muffins", 35, false, 10, R.drawable.big_11, "Lemon muffins are a classic American dessert. They have a delicate lemon taste and aroma. Lemon zest gives the muffins a pleasant bitterness, and lemon juice adds sourness. These muffins go great with tea or coffee."),
            new Item(R.drawable.muffin_4, "Vanilla muffins with berries", 40, false, 11, R.drawable.big_12, "Vanilla muffins with berries are a tender and airy treat. Berries give the muffins freshness and juiciness, and vanilla gives a pleasant aroma. These muffins are an excellent choice for tea or dessert after lunch.")};

    Item[][] sweets = new Item[][] {cakes, cupcakes, muffins, cheesecakes};
    int pageNumber;

    static PageFragment newInstance(int page) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragments, null);
        RecyclerView recyclerView = view.findViewById(R.id.items);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        Handler handler = new Handler(Looper.getMainLooper());
        Runnable run = new Runnable() {
            @Override
            public void run() {
                fillListFav(sweets[pageNumber], view.getContext(), pageNumber);
                ItemAdapter itemAdapter = new ItemAdapter(new ArrayList<>(Arrays.asList(sweets[pageNumber])), view.getContext(), false);
                recyclerView.setAdapter(itemAdapter);
            }
        };
        handler.post(run);

        return view;
    }

    private void fillListFav(Item[] list, Context ctx, int step){
        String favsInfo = ctx.getSharedPreferences("FAV", MODE_PRIVATE).getString("FAV", "0000000000000000");
        for (int i = 0; i < list.length; ++i) {
            list[i].setFav(favsInfo.charAt(i+ 4 * step) == '1');
        }
    }

}
