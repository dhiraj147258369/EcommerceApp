package com.example.dstore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.dynamic.IFragmentWrapper;

import java.util.List;

import static com.example.dstore.DeliveryActivity.SELECT_ADDRESS;
import static com.example.dstore.MyAccountFragment.MANAGE_ADDRESS;
import static com.example.dstore.MyAddressesActivity.refreshItem;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.viewHolder> {

    private List<AddressesModel> addressesModelList;

    private int MODE;
    private int preSelectedPosition;

    public AddressesAdapter(List<AddressesModel> addressesModelList,int MODE) {
        this.addressesModelList = addressesModelList;
        this.MODE = MODE;
        preSelectedPosition = DBqueries.selectedAddress;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
      String name =addressesModelList.get(position).getFullname();
        String address =addressesModelList.get(position).getAddress();
        String pin =addressesModelList.get(position).getPincode();
        Boolean selected =addressesModelList.get(position).getSelected();

   holder.setData(name,address,pin,selected,position);
    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private TextView fullname;
        private TextView address;
        private TextView pincode;
        private ImageView icon;
        private LinearLayout optionContainer;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.fullnamee);
            address = itemView.findViewById(R.id.full_address);
            pincode = itemView.findViewById(R.id.pincodee);
            icon = itemView.findViewById(R.id.icon);
            optionContainer = itemView.findViewById(R.id.option_container);

        }

        private void setData(String username, String useraddress, String userpincode, Boolean selected, int position){
            fullname.setText(username);
            address.setText(useraddress);
            pincode.setText(userpincode);

            if (MODE == SELECT_ADDRESS){
                    icon.setImageResource(R.drawable.ic_check);
                    if (selected){
                        icon.setVisibility(View.VISIBLE);
                        preSelectedPosition = position;
                    }else{
                        icon.setVisibility(View.GONE);
                    }
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (preSelectedPosition != position){
                                addressesModelList.get(position).setSelected(true);
                                addressesModelList.get(preSelectedPosition).setSelected(false);
                                refreshItem(preSelectedPosition,position);
                                preSelectedPosition = position;
                                DBqueries.selectedAddress = position;
                            }
                        }
                    });
            }else if(MODE == MANAGE_ADDRESS){
                 optionContainer.setVisibility(View.GONE);
                icon.setImageResource(R.drawable.ic_vertical_dots);

                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionContainer.setVisibility(View.VISIBLE);
                        refreshItem(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition= position;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshItem(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition = -1;
                    }
                });
            }
        }
    }
}
