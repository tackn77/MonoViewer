package jp.chiba.tackn.monoviewer.map;
/**
 * CursorLoaderの結果取得後の処理で配列として利用
 * 現在時刻の列車についての情報
 */
class NowTrainData {
    /**
     * 駅名
     */
    public String Station;
    /**
     * 上り・下り 0:上り 1:下り
     */
    public int UpDown;
    /**
     * 列車運行情報ID
     */
    public int Table_No;
    /**
     * 時刻(時間)
     */
    public int Hour;
    /**
     * 時刻(分)
     */
    public int Minute;
    /**
     * 次の駅情報
     */
    public NowTrainData after;

    public NowTrainData(String Station, int UpDown, int Hour, int Minute, int Table_No, NowTrainData after) {
        this.Station = Station;
        this.UpDown = UpDown;
        this.Table_No = Table_No;
        this.Hour = Hour;
        this.Minute = Minute;
        this.after = after;
    }

    /**
     * 比較
     *
     * @param o 比較用オブジェクト
     * @return 同じかどうか
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != NowTrainData.class) return false;
        NowTrainData comparison = (NowTrainData) o;
        boolean check;

        check =          this.Station.equals(comparison.Station);
        check = check && this.UpDown == comparison.UpDown;
        check = check && this.Table_No == comparison.Table_No;
        check = check && this.Hour == comparison.Hour;
        check = check && this.Minute == comparison.Minute;
        if (this.after != null) {
                return comparison.after != null && check && this.after.equals(comparison.after);
        } else {
                return comparison.after == null && check;
        }
    }

    /**
     * ハッシュ用の種
     */
    private static final int hashValue = 31;

    /**
     * ハッシュマップ計算
     *
     * @return ハッシュ値
     */
    @Override
    public int hashCode() {

        int result = 17;

        result = this.Station == null ? hashValue * result : hashValue * result + this.Station.hashCode();
        result = hashValue * result + this.UpDown;
        result = hashValue * result + this.Table_No;
        result = hashValue * result + this.Hour;
        result = hashValue * result + this.Minute;
        result = this.after == null ? hashValue * result : hashValue * result + this.after.hashCode();
        return result;
    }
}
