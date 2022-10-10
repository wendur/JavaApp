package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int playerId;
    private String nickname;
    private List<Progresses> progresses = new ArrayList<>();
    private List<Currencies> currencies = new ArrayList<>();
    private List<Items> items = new ArrayList<>();

    public int getPlayerId() {
        return playerId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProgresses(List<Progresses> progresses) {
        this.progresses = progresses;
    }

    public void setCurrencies(List<Currencies> currencies) {
        this.currencies = currencies;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public void addProgress(Progresses prog) {
        progresses.add(prog);
    }

    public void addCurrency(Currencies cur) {
        currencies.add(cur);
    }

    public void addItem(Items item) {
        items.add(item);
    }

    public List<Progresses> getProgresses() {
        return progresses;
    }

    public List<Currencies> getCurrencies() {
        return currencies;
    }

    public List<Items> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", nickname='" + nickname + '\'' +
                ", progresses=" + progresses  + '\n'+
                ", currencies=" + currencies  + '\n'+
                ", items=" + items +
                '}';
    }

    /*
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("***** Player Details *****\n");
        sb.append("ID="+getPlayerId()+"\n");
        sb.append("Nickname="+getNickname()+"\n");
        sb.append("Progresses="+ Arrays.toString(getProgresses()) +"\n");
        sb.append("Currencies="+ Arrays.toString(getCurrencies()) +"\n");
        sb.append("Items="+ Arrays.toString(getItems()) +"\n");
        sb.append("*****************************\n");

        return sb.toString();
    }

     */
}
