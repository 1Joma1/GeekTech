package com.joma.geektech.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joma.geektech.R;
import com.joma.geektech.model.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private List<User> list = new ArrayList<>();
    private onItemClick onItemClick;

    public UsersAdapter(UsersAdapter.onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setList(List<User> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void addItem(User user) {
        this.list.add(user);
        notifyDataSetChanged();
    }

    public List<User> getList() {
        return this.list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name, password, thread, coin, role;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.item_user_profile);
            name = itemView.findViewById(R.id.item_user_name);
            password = itemView.findViewById(R.id.item_user_password);
            thread = itemView.findViewById(R.id.item_user_thread);
            coin = itemView.findViewById(R.id.item_user_coin);
            role = itemView.findViewById(R.id.item_user_role);
            itemView.setOnLongClickListener(view -> {
                AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext());
                alert.setTitle(itemView.getContext().getResources().getString(R.string.are_you_sure_to_delet));
                alert.setNeutralButton(itemView.getContext().getResources().getString(R.string.cancel), null);
                alert.setNegativeButton(itemView.getContext().getResources().getString(R.string.edit), (dialogInterface, i) -> {
                    onItemClick.edit(getAdapterPosition());
                });
                alert.setPositiveButton(itemView.getContext().getResources().getString(R.string.delete), (dialogInterface, i) -> FirebaseFirestore.getInstance().collection("User").document(list.get(getAdapterPosition()).getId()).delete());
                alert.show();
                return true;
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), GeekCoinHistoryActivity.class);
                    intent.putExtra("id", list.get(getAdapterPosition()).getId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public void bind(User user) {
            Context context = name.getContext();
            if (user.getProfile() != null) {
                Glide.with(image.getContext())
                        .load(user.getProfile())
                        .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(1000)))
                        .into(image);
            }
            name.setText(context.getResources().getString(R.string.name)+": " + user.getName());
            password.setText(context.getResources().getString(R.string.password)+": " + user.getPassword());
            thread.setText(context.getResources().getString(R.string.thread)+": " + user.getGroup());
            coin.setText(context.getResources().getString(R.string.geekcoin)+": " + user.getCoin());
            if (user.isAdmin()) {
                role.setText(context.getResources().getString(R.string.admin));
            } else if (user.isTeacher()) {
                role.setText(context.getResources().getString(R.string.teacher));
            } else {
                role.setText(context.getResources().getString(R.string.student));
            }
        }
    }

    public interface onItemClick{
        void edit(int pos);
    }
}