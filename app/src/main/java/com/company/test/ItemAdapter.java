package com.company.test;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    public static String replaceCharAt(String str, int index, char newChar) {
        // Проверка на допустимость индекса
        if (index < 0 || index >= str.length()) {
            throw new IllegalArgumentException("Некорректный индекс: " + index);
        }

        char[] charArray = str.toCharArray();
        charArray[index] = newChar;
        return new String(charArray);
    }
    private List<Item> items;
    private Context context;
    boolean isFav;

    public ItemAdapter(List<Item> items, Context context, boolean isFav) {
        this.items = items;
        this.context = context;
        this.isFav = isFav;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Item item = items.get(position);
        if (position == 0 && !isFav) {
            holder.top.setImageResource(R.drawable.top);
        }
        holder.title.setText(item.getTitle());
        holder.image.setBackgroundResource(item.getImage());
        holder.price.setText(item.getPrice() + "");
        if (item.getFav()) {
            holder.fav.setBackgroundResource(R.drawable.fav_pressed);
        }
        else {
            holder.fav.setBackgroundResource(R.drawable.fav);
        }

        if (!isFav) {
            holder.description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.isBack = false;
                    MainActivity.getDark().setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });

                    ConstraintLayout card = MainActivity.getCard();
                    if (position == 0 && !isFav) {
                        card.findViewById(R.id.cardTop).setAlpha(1.0f);
                    }

                    ImageView img = card.findViewById(R.id.imageProduct);
                    TextView title = card.findViewById(R.id.cardTitle);
                    TextView desc = card.findViewById(R.id.cardDesc);
                    card.findViewById(R.id.cardAddToCart).clearAnimation();
                    card.findViewById(R.id.cardAddToCart).setAlpha(0.0f);

                    img.setBackgroundResource(item.getImageCard());
                    title.setText(item.getTitle());
                    desc.setText(item.getDesc());
                    ImageButton back = card.findViewById(R.id.cardButtonClose);
                    ImageButton fav = card.findViewById(R.id.cardButtonFav);
                    if (item.getFav()) {
                        fav.setBackgroundResource(R.drawable.fav_pressed);
                    } else {
                        fav.setBackgroundResource(R.drawable.fav);
                    }
                    ImageButton plus = card.findViewById(R.id.cardButtonPlus);
                    ImageButton minus = card.findViewById(R.id.cardButtonMinus);
                    ImageView dark = MainActivity.getDark();
                    TextView count = card.findViewById(R.id.cardCount);
                    int[] countInt = {1};
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainActivity.isBack = true;
                            Handler hand = new Handler(Looper.getMainLooper());

                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    dark.setZ(-1);
                                }
                            };
                            holder.hand.removeCallbacks(holder.runnable);
                            card.findViewById(R.id.cardAddToCart).clearAnimation();
                            card.findViewById(R.id.cardAddToCart).setAlpha(0.0f);
                            card.findViewById(R.id.cardTop).setAlpha(0.0f);


                            card.animate().translationY(card.getHeight()).setDuration(300);
                            dark.animate().alpha(0).setDuration(300);
                            hand.postDelayed(runnable, 300);
                        }
                    });
                    fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = holder.getAdapterPosition();
                            SharedPreferences shp = context.getSharedPreferences("FAV", MODE_PRIVATE);
                            SharedPreferences.Editor editor = shp.edit();

                            String favsInfo = shp.getString("FAV", "0000000000000000");

                            if (favsInfo.charAt(items.get(position).getId()) == '0') {
                                item.setFav(true);
                                v.setBackgroundResource(R.drawable.fav_for_card_pressed);
                                holder.fav.setBackgroundResource(R.drawable.fav_for_card_pressed);
                                String newStr = replaceCharAt(favsInfo, items.get(position).getId(), '1');
                                editor.putString("FAV", newStr);
                                editor.apply();
                            } else {
                                item.setFav(false);
                                v.setBackgroundResource(R.drawable.fav);
                                holder.fav.setBackgroundResource(R.drawable.fav);
                                if (isFav) {
                                    if (position != RecyclerView.NO_POSITION) {
                                        // Use Handler to delay the removal
                                        new Handler(Looper.getMainLooper()).post(() -> {
                                            items.remove(position);
                                            notifyItemRemoved(position);
                                        });
                                    }
                                }
                                String newStr = replaceCharAt(favsInfo, items.get(position).getId(), '0');
                                editor.putString("FAV", newStr);
                                editor.apply();
                            }
                        }
                    });
                    plus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (countInt[0] < 1000) {
                                ++countInt[0];
                                count.setText(countInt[0] + "");
                            }
                        }
                    });
                    minus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (countInt[0] > 1) {
                                --countInt[0];
                                count.setText(countInt[0] + "");
                            }
                        }
                    });

                    ImageButton add = card.findViewById(R.id.cardButtonAdd);

                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences sharedPreferences = context.getSharedPreferences("basket", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            String backetValue = sharedPreferences.getString("basket", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
                            String[] stringArray = backetValue.split(",");
                            int[] basketValueInt = new int[stringArray.length];
                            for (int i = 0; i < stringArray.length; i++) {
                                basketValueInt[i] = Integer.parseInt(stringArray[i]);
                            }
                            ConstraintLayout card = MainActivity.getCard();
                            TextView count = card.findViewById(R.id.cardCount);

                            if (basketValueInt[item.getId()] == 0) {
                                holder.counter.setText((Integer.parseInt(holder.counter.getText().toString()) + 1) + "");
                            }

                            basketValueInt[item.getId()] += Integer.parseInt(count.getText().toString());
                            StringBuilder sb = new StringBuilder();
                            for (int i : basketValueInt) {
                                sb.append(i).append(",");
                            }
                            editor.putString("basket", sb.toString());
                            editor.apply();

                            ImageView fon = card.findViewById(R.id.cardAddToCart);
                            if (!holder.isAnim) {
                                holder.isAnim = true;

                                fon.animate().alpha(1.0f).setDuration(1000);
                                holder.hand.postDelayed(holder.runnable, 2000);

                            }
                        }
                    });
                    count.setText("1");

                    card.setZ(900);
                    dark.setZ(899);
                    card.animate().translationY(-card.getHeight()).setDuration(300);
                    dark.animate().alpha(0.7f).setDuration(300);

                }
            });
        }

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                SharedPreferences shp = context.getSharedPreferences("FAV", MODE_PRIVATE);
                SharedPreferences.Editor editor = shp.edit();

                String favsInfo = shp.getString("FAV", "0000000000000000");

                if (favsInfo.charAt(items.get(position).getId()) == '0') {
                    item.setFav(true);
                    v.setBackgroundResource(R.drawable.fav_for_card_pressed);
                    String newStr = replaceCharAt(favsInfo, items.get(position).getId(),  '1');
                    editor.putString("FAV", newStr);
                    editor.apply();
                }
                else {
                    item.setFav(false);
                    v.setBackgroundResource(R.drawable.fav);
                    if (isFav) {
                        if (position != RecyclerView.NO_POSITION) {
                            // Use Handler to delay the removal
                            new Handler(Looper.getMainLooper()).post(() -> {
                                items.remove(position);
                                notifyItemRemoved(position);
                            });
                        }
                    }
                    String newStr = replaceCharAt(favsInfo, items.get(position).getId(),  '0');
                    editor.putString("FAV", newStr);
                    editor.apply();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView price;
        ImageView image;
        ImageButton fav;
        ImageButton description;
        ImageView top;
        ImageView fon;
        boolean isAnim = false;
        TextView counter;
        Handler hand = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ConstraintLayout card = MainActivity.getCard();
                card.findViewById(R.id.cardAddToCart).animate().alpha(0).setDuration(1000);
                isAnim = false;
            }
        };

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            price = itemView.findViewById(R.id.itemPrice);
            image = itemView.findViewById(R.id.itemImage);
            description = itemView.findViewById(R.id.itemBackground);
            fav = itemView.findViewById(R.id.itemButtonFav);
            top = itemView.findViewById(R.id.itemTop);
            counter = MainActivity.getCounterBasket();
        }
    }
}
