package ClientData;

public class Client  {

    String ip;
    String name;
    int coins;
    int totalcoins;

    public Client() {

    }

    public Client(String ip, String name, int coins, int totalcoins) {

        this.ip = ip;
        this.name = name;
        this.coins = coins;
        this.totalcoins = totalcoins;

    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public int getCoins() {
        return coins;
    }

    public int getTotalCoins() {
        return totalcoins;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setTotalcoins(int totalcoins) {
        this.totalcoins = totalcoins;
    }
}
