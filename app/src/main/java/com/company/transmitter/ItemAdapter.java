package com.company.transmitter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

        // Устанавливаем верхний ресурс только один раз для первой позиции, если это не избранное
        if (position == 0 && !isFav) {
            holder.top.setImageResource(R.drawable.top);
        } else {
            holder.top.setImageResource(0); // Сбрасываем изображение, если это не первая позиция
        }

        // Устанавливаем заголовок и изображение только если они изменились
        if (!holder.title.getText().toString().equals(item.getTitle())) {
            holder.title.setText(item.getTitle());
        }

        if (holder.image.getTag() == null || (int) holder.image.getTag() != item.getImage()) {
            holder.image.setImageResource(item.getImage());
            holder.image.setTag(item.getImage());
        }



        holder.price.setText(item.getPrice() + "");

        // Устанавливаем фоновый ресурс для избранного
        if (item.getFav()) {
            holder.fav.setBackgroundResource(R.drawable.fav_pressed);
        } else {
            holder.fav.setBackgroundResource(R.drawable.fav);
        }

        if (!isFav) {
            holder.description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInfoMenu(context, holder, position, item);
                    MainActivity.isBack = false;
                }
            });
        }

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                SharedPreferences shp = context.getSharedPreferences("FAV", MODE_PRIVATE);
                SharedPreferences.Editor editor = shp.edit();
                String favsInfo = shp.getString("FAV", "0000000000000000");
                if (favsInfo.charAt(items.get(position).getId()) == '0') {
                    item.setFav(true);
                    v.setBackgroundResource(R.drawable.fav_for_card_pressed);
                    String newStr = replaceCharAt(favsInfo, items.get(position).getId(), '1');
                    editor.putString("FAV", newStr);
                    editor.apply();
                } else {
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
                    String newStr = replaceCharAt(favsInfo, items.get(position).getId(), '0');
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
        ImageView description;
        ImageView top;
        boolean isAnim = false;
        TextView counter;
        Handler hand = new Handler(Looper.getMainLooper());
        Runnable runnable;
        ImageView lines;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            price = itemView.findViewById(R.id.itemPrice);
            image = itemView.findViewById(R.id.itemImage);
            description = itemView.findViewById(R.id.itemBackground);
            fav = itemView.findViewById(R.id.itemButtonFav);
            top = itemView.findViewById(R.id.itemTop);
            lines = itemView.findViewById(R.id.lines);
            counter = MainActivity.counterBasket;
        }
    }

    public void showInfoMenu(Context context, ViewHolder holder, int position, Item item) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.setContentView(R.layout.info_menu);
        holder.runnable =  new Runnable() {
            @Override
            public void run() {
                dialog.findViewById(R.id.cardAddToCart).animate().alpha(0).setDuration(1000);
                holder.isAnim = false;
            }
        };


        if (position == 0) { dialog.findViewById(R.id.cardTop).setAlpha(1.0f); }
        ImageView img = dialog.findViewById(R.id.imageProduct);
        TextView title = dialog.findViewById(R.id.cardTitle);
        TextView desc = dialog.findViewById(R.id.cardDesc);
        dialog.findViewById(R.id.cardAddToCart).clearAnimation();
        dialog.findViewById(R.id.cardAddToCart).setAlpha(0.0f);
        img.setBackgroundResource(item.getImageCard());
        title.setText(item.getTitle());
        desc.setText(item.getDesc());
        ImageButton back = dialog.findViewById(R.id.cardButtonClose);
        ImageButton fav = dialog.findViewById(R.id.cardButtonFav);
        if (item.getFav()) {
            fav.setBackgroundResource(R.drawable.fav_pressed);
        } else {
            fav.setBackgroundResource(R.drawable.fav);
        }
        ImageButton plus = dialog.findViewById(R.id.cardButtonPlus);
        ImageButton minus = dialog.findViewById(R.id.cardButtonMinus);
        TextView count = dialog.findViewById(R.id.cardCount);
        int[] countInt = {1};

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isBack = true;

                holder.hand.removeCallbacks(holder.runnable);
                dialog.findViewById(R.id.cardAddToCart).clearAnimation();
                dialog.findViewById(R.id.cardAddToCart).setAlpha(0.0f);
                dialog.findViewById(R.id.cardTop).setAlpha(0.0f);

                dialog.dismiss();
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

        ImageButton add = dialog.findViewById(R.id.cardButtonAdd);

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
                TextView count = dialog.findViewById(R.id.cardCount);


                holder.counter.setText((Integer.parseInt(holder.counter.getText().toString()) + Integer.parseInt(count.getText().toString())) + "");


                basketValueInt[item.getId()] += Integer.parseInt(count.getText().toString());
                StringBuilder sb = new StringBuilder();
                for (int i : basketValueInt) {
                    sb.append(i).append(",");
                }
                editor.putString("basket", sb.toString());
                editor.apply();

                ImageView fon = dialog.findViewById(R.id.cardAddToCart);
                if (!holder.isAnim) {
                    holder.isAnim = true;

                    fon.animate().alpha(1.0f).setDuration(1000);
                    holder.hand.postDelayed(holder.runnable, 2000);

                }
            }
        });
        count.setText("1");

        Window window = dialog.getWindow();

        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);

            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            wlp.dimAmount = 0.7f;

            window.setAttributes(wlp);

        }
        dialog.show();
    }

}