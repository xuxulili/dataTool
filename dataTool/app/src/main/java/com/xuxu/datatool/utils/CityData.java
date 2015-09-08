package com.xuxu.datatool.utils;

import com.xuxu.datatool.Model.province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/15.
 */
public class CityData {
    public static List<province> provincesList;
    public  static List<province> getList(){
        provincesList = new ArrayList<>();
        province pro = new province();
        pro.setPlace(BJ);
        pro.setTag("BJ");
        provincesList.add(pro);

        province pro1 = new province();
        pro1.setPlace(GD);
        pro1.setTag("GD");
        provincesList.add(pro1);

        province pro2 = new province();
        pro2.setPlace(SD);
        pro2.setTag("SD");
        provincesList.add(pro2);

        province pro3 = new province();
        pro3.setPlace(JS);
        pro3.setTag("JS");
        provincesList.add(pro3);

        province pro4 = new province();
        pro4.setPlace(HA);
        pro4.setTag("HA");
        provincesList.add(pro4);

        province pro5 = new province();
        pro5.setPlace(SH);
        pro5.setTag("SH");
        provincesList.add(pro5);

        province pro6 = new province();
        pro6.setPlace(HE);
        pro6.setTag("HE");
        provincesList.add(pro6);

        province pro7 = new province();
        pro7.setPlace(ZJ);
        pro7.setTag("ZJ");
        provincesList.add(pro7);

        province pro8 = new province();
        pro8.setPlace(XG);
        pro8.setTag("XG");
        provincesList.add(pro8);

        province pro9 = new province();
        pro9.setPlace(SN);
        pro9.setTag("SN");
        provincesList.add(pro9);

        province pro10 = new province();
        pro10.setPlace(HN);
        pro10.setTag("HN");
        provincesList.add(pro10);

        province pro11 = new province();
        pro11.setPlace(CQ);
        pro11.setTag("CQ");
        provincesList.add(pro11);

        province pro12 = new province();
        pro12.setPlace(FJ);
        pro12.setTag("FJ");
        provincesList.add(pro12);

        province pro13 = new province();
        pro13.setPlace(TJ);
        pro13.setTag("TJ");
        provincesList.add(pro13);

        province pro14 = new province();
        pro14.setPlace(YN);
        pro14.setTag("YN");
        provincesList.add(pro14);

        province pro15 = new province();
        pro15.setPlace(SC);
        pro15.setTag("SC");
        provincesList.add(pro15);

        province pro16 = new province();
        pro16.setPlace(GX);
        pro16.setTag("GX");
        provincesList.add(pro16);

        province pro17 = new province();
        pro17.setPlace(AH);
        pro17.setTag("AH");
        provincesList.add(pro17);

        province pro18 = new province();
        pro18.setPlace(HI);
        pro18.setTag("HI");
        provincesList.add(pro18);

        province pro19 = new province();
        pro19.setPlace(JX);
        pro19.setTag("JX");
        provincesList.add(pro19);

        province pro20 = new province();
        pro20.setPlace(HB);
        pro20.setTag("HB");
        provincesList.add(pro20);

        province pro21 = new province();
        pro21.setPlace(SX);
        pro21.setTag("SX");
        provincesList.add(pro21);

        province pro22 = new province();
        pro22.setPlace(LN);
        pro22.setTag("LN");
        provincesList.add(pro22);

        province pro23 = new province();
        pro23.setPlace(TW);
        pro23.setTag("TW");
        provincesList.add(pro23);

        province pro24 = new province();
        pro24.setPlace(HL);
        pro24.setTag("HL");
        provincesList.add(pro24);

        province pro25 = new province();
        pro25.setPlace(NM);
        pro25.setTag("NM");
        provincesList.add(pro25);

        province pro26 = new province();
        pro26.setPlace(AM);
        pro26.setTag("AM");
        provincesList.add(pro26);

        province pro27 = new province();
        pro27.setPlace(GZ);
        pro27.setTag("GZ");
        provincesList.add(pro27);

        province pro28 = new province();
        pro28.setPlace(GS);
        pro28.setTag("GS");
        provincesList.add(pro28);

        province pro29 = new province();
        pro29.setPlace(QH);
        pro29.setTag("QH");
        provincesList.add(pro29);

        province pro30 = new province();
        pro30.setPlace(XJ);
        pro30.setTag("XJ");
        provincesList.add(pro30);

        province pro31 = new province();
        pro31.setPlace(XZ);
        pro31.setTag("XZ");
        provincesList.add(pro31);

        province pro32 = new province();
        pro32.setPlace(JL);
        pro32.setTag("JL");
        provincesList.add(pro32);

        province pro33 = new province();
        pro33.setPlace(NX);
        pro33.setTag("NX");
        provincesList.add(pro33);
        return provincesList;
    }

    //湖南 HN 河南HA 海南HI 河北HE 湖北HB  山西SX 陕西SN
    public static String NX = "银川市　 吴忠市　 中卫市　 石嘴山市　 固原市";
    public static String JL = "吉林市　 长春市　 白山市　 白城市　 延边州　 松原市　 辽源市　 通化市　 四平市";
    public static String XZ = "拉萨市　 山南地区　 林芝地区　 日喀则地区　 阿里地区　 昌都地区　 那曲地区　";
    public static String XJ = "乌鲁木齐市　 伊犁州　 昌吉州　 石河子市　 哈密地区　 阿克苏地区　 巴音郭楞州　 喀什地区　 塔城地区　 克拉玛依市　 和田地区　 阿勒泰州　 吐鲁番地区　 阿拉尔市　 博尔塔拉州　 五家渠市　 克孜勒苏州　 图木舒克市　";
    public static String QH = "西宁市　 海西州　 海东地区　 海北州　 果洛州　 玉树州　 黄南藏族自治州　";
    public static String GS = "兰州市　 天水市　 庆阳市　 武威市　 酒泉市　 张掖市　 陇南地区　 白银市　 定西地区　 平凉市　 嘉峪关市　 临夏回族自治州　 金昌市　 甘南州　";
    public static String GZ = "贵阳市　 黔东南州　 黔南州　 遵义市　 黔西南州　 毕节地区　 铜仁地区　 安顺市　 六盘水市";
    public static String AM = "澳门";
    public static String NM = "赤峰市　 包头市　 通辽市　 呼和浩特市　 乌海市　 鄂尔多斯市　 呼伦贝尔市　 兴安盟　 巴彦淖尔盟　 乌兰察布盟　 锡林郭勒盟　 阿拉善盟　";
    public static String HL = "齐齐哈尔市　 哈尔滨市　 大庆市　 佳木斯市　 双鸭山市　 牡丹江市　 鸡西市　 黑河市　 绥化市　 鹤岗市　 伊春市　 大兴安岭地区　 七台河市　";
    public static String TW = "台北市";//　 高雄市　 台中市　 新竹市　 基隆市　 台南市　 嘉义市
    public static String LN = "大连市　 沈阳市　 丹东市　 辽阳市　 葫芦岛市　 锦州市　 朝阳市　 营口市　 鞍山市　 抚顺市　 阜新市　 本溪市　 盘锦市　 铁岭市";
    public static String SX = "太原市　 大同市　 运城市　 长治市　 晋城市　 忻州市　 临汾市　 吕梁市　 晋中市　 阳泉市　 朔州市";
    public static String HB = "武汉市　 宜昌市　 襄樊市　 荆州市　 恩施州　 孝感市　 黄冈市　 十堰市　 咸宁市　 黄石市　 仙桃市　 随州市　 天门市　 荆门市　 潜江市　 鄂州市　 神农架林区　";
    public static String JX = "南昌市　 赣州市　 上饶市　 吉安市　 九江市　 新余市　 抚州市　丰城市 宜春市 　 景德镇市　 萍乡市　 鹰潭市";
    public static String HI = "三亚市　 海口市　 琼海市　 文昌市　 东方市　 昌江县　 陵水县　 乐东县　 五指山市　 保亭县　 澄迈县　 万宁市　 儋州市　 临高县　 白沙县　 定安县　 琼中县　 屯昌县";
    public static String AH = "芜湖市　 合肥市　 六安市　 宿州市　 阜阳市　 安庆市　 马鞍山市　 蚌埠市　 淮北市　 淮南市　 宣城市　 黄山市　 铜陵市　 亳州市　 池州市　 巢湖市　 滁州市　";
    public static String GX = "贵港市　 玉林市　 北海市　 南宁市　 柳州市　 桂林市　 梧州市　 钦州市　 来宾市　 河池市　 百色市　 贺州市　 崇左市　 防城港市　";
    public static String SC = "成都市　 绵阳市　 广元市　 达州市　 南充市　 德阳市　 广安市　 阿坝州　 巴中市　 遂宁市　 内江市　 凉山州　 攀枝花市　 乐山市　 自贡市　 泸州市　 雅安市　 宜宾市　 资阳市　 眉山市　 甘孜州　";
    public static String YN = "昆明市　 红河州　 大理州　 文山州　 德宏州　 曲靖市　 昭通市　 楚雄州　 保山市　 玉溪市　 丽江地区　 临沧地区　 思茅地区　 西双版纳州　 怒江州　 迪庆州　";
    public static String TJ = "天津市 北辰区　 　　 西青区　 津南区　 东丽区　 武清区　 宝坻区　 大港区　 汉沽区　 静海县　 宁河县　 塘沽区　 蓟县　";

    public static String FJ = "漳州市　 泉州市　 厦门市　 福州市　 莆田市　 宁德市　 三明市　 南平市　 龙岩市";
    public static String CQ = "重庆市 　 渝北区　 　 万州区　 永川市　 酉阳县　 北碚区　 涪陵区　 秀山县　 巴南区　  石柱县　 忠县　 合川市　 大渡口区　 开县　 长寿区　 荣昌县　 云阳县　 梁平县　 潼南县　 江津市　 彭水县　 璧山县　 綦江县　 大足县　 黔江区　 巫溪县　 巫山县　 垫江县　 丰都县　 武隆县　 万盛区　 铜梁县　 南川市　 奉节县　 双桥区　 城口县";
    public static String HN = "长沙市　 邵阳市　 常德市　 衡阳市　 株洲市　 湘潭市　 永州市　 岳阳市　 怀化市　 郴州市　 娄底市　 益阳市　 张家界市　 湘西州　";
    public static String SN = "西安市　 咸阳市　 宝鸡市　 汉中市　 渭南市　 安康市　 榆林市　 商洛市　 延安市　 铜川市";
    public static String XG = "香港";
    public static String ZJ = "温州市　 宁波市　 杭州市　 台州市　 嘉兴市　 金华市　 湖州市　 绍兴市　 舟山市　 丽水市　 衢州市　";
    public static String HE = "石家庄市　 唐山市　 保定市　 邯郸市　 邢台市　 　 沧州市　 秦皇岛市　 张家口市　 衡水市　 廊坊市　 承德市";
    public static String SH = "上海市   松江区　 宝山区　 金山区　 嘉定区　 南汇区　 青浦区　  浦东区　 奉贤区　 闵行区　　 崇明县　";
    public static String HA = "郑州市　 南阳市　 新乡市　 安阳市　 洛阳市　 信阳市　 平顶山市　 周口市　 商丘市　 开封市　 焦作市　 驻马店市　 濮阳市　 三门峡市　 漯河市　 许昌市　 鹤壁市　 济源市　";
    public static String JS = "苏州市　 徐州市　 盐城市　 无锡市　 南京市　 南通市　 连云港市　 常州市　 扬州市　 镇江市　 淮安市　 泰州市　 宿迁市　";
    public static String SD = "济南市　 青岛市　 临沂市　 济宁市　 菏泽市　 烟台市　 泰安市　 淄博市　 潍坊市　 日照市　 威海市　 滨州市　 东营市　 聊城市　 德州市　 莱芜市　 枣庄市　";
    public static String BJ = "北京市 　 　 通州区　 房山区　 　 昌平区　 大兴区　 顺义区　 延庆县　 　密云县　 　 门头沟区　 平谷区　怀柔区";
    public static String GD = "东莞市　 广州市　 中山市　 深圳市　 惠州市　 江门市　 珠海市　 汕头市　 佛山市　 湛江市　 河源市　 肇庆市　 潮州市　 清远市　 韶关市　 揭阳市　 阳江市　 云浮市　 茂名市　 梅州市　 汕尾市　";
}
