package dto;

/**
 * Created by lucas on 05/11/2017.
 */
public class TickerInfoDTO {
    private String ticker;
    private String ano;
    private Double size;
    private Double bmme;
    private Double op;
    private Double inv;

    public TickerInfoDTO() {
    }

    public TickerInfoDTO(String ticker, String ano, Double size, Double bmme, Double op, Double inv) {
        this.ticker = ticker;
        this.ano = ano;
        this.size = size;
        this.bmme = bmme;
        this.op = op;
        this.inv = inv;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Double getBmme() {
        return bmme;
    }

    public void setBmme(Double bmme) {
        this.bmme = bmme;
    }

    public Double getOp() {
        return op;
    }

    public void setOp(Double op) {
        this.op = op;
    }

    public Double getInv() {
        return inv;
    }

    public void setInv(Double inv) {
        this.inv = inv;
    }

    @Override
    public String toString() {
        return "TickerInfoDTO{" +
                "ticker='" + ticker + '\'' +
                ", ano='" + ano + '\'' +
                ", size=" + size +
                ", bmme=" + bmme +
                ", op=" + op +
                ", inv=" + inv +
                '}';
    }
}
