# 鉱石採掘ゲーム（Ore Mining Game）
デイトラJavaコース ミニゲーム開発提出課題 鉱石採掘ゲームです。

## ゲーム概要
- Minecraft内で特定の鉱石を採掘するとスコアが加算されるゲームをプレイできます。  
- 制限時間は2分です。  
- 縦30ブロック、横30ブロック、高さ30ブロックのフィールドが自動生成されます。__大事な建築物を上書きしてしまう恐れがありますので、何もない十分な広さの場所でコマンドを叩いてください。__

## ゲーム詳細
- コマンドで`/gamestart`と入力しエンターキーを押すとゲームを開始します。  
- あなたの大事なインベントリ内のアイテムと装備は目の前に出現した2つの宝箱の中にしまっておきます。それから右手にピッケルを渡します。  
- 5秒間のカウントダウンの後、2分間のカウントがスタートします。    
- 現在の得点はいつでも画面右のスコアボードで見ることができます。ついでにどのブロックを掘ったら何点もらえるのかも書いておきました。参考にしてください。
- 得点はDBに記録しておきます。最高得点を目指してプレイしてみてください！  
>[!WARNING]
>- 砂ブロックは時にあなたを手助けしてくれます。が、頭上に降ってくるとダメージを負います。体力と空腹値はゲーム開始時に全回復するため死なないとは思いますが……、万が一のこともあるので気を付けましょう。
>- 掘った鉱石とピッケルはお持ち帰りいただいて構いません。ドアのご用意がございませんのでお帰りの際はお近くの壁を壊して脱出してください。お忘れ物が多くなっておりますので、最初に生成しました2つの宝箱の中身とお手荷物を今一度ご確認のうえ、ご退出ください。　　

## プレイ説明動画


https://github.com/user-attachments/assets/f1c3df88-dab0-443f-a3fc-b504e358083e



https://github.com/user-attachments/assets/7dcbc294-3ab7-405a-a259-2ff179ea429a



https://github.com/user-attachments/assets/3ad6b3b6-a9a5-4b35-b6e1-d77e6017ce45



>[!TIP]
>### データベース設計
>|属性|設定値|
>|----|----|
>|ユーザー名|※|
>|パスワード|※|
>|URL|※|
>|データベース名|spigot_server|
>|テーブル名|player_score|
>### データベースの接続方法
>
>1. 自身のローカル環境でMySQLに接続してください。
>
>2. 以下のコマンドを順に実行してください。
>```
>CREATE DATABASE spigot_server;
>```
>```
>USE spigot_server;
>```
>```
>CREATE TABLE  player_score(id int auto_increment, score int, registered_at datetime, player_name varchar(255), primary key(id)) DEFAULT CHARSET=utf8;
>```
>
>3. MySQLのurl,username,passwordはご自身のローカル環境に合わせてご使用ください。(mybatis-config.xmlで設定します。)　　


## 得点
- 鉄鉱石、石炭　1ポイント  
- レッドストーン鉱石　5ポイント  
- 銅鉱石　10ポイント  
- 金鉱石　20ポイント  
- ラピスラズリ鉱石　30ポイント  
- エメラルド鉱石　80ポイント  
- ダイヤモンド鉱石 100ポイント  



## 対応バージョン
- Java版 Minecraft 1.20.4
- Spigot 1.20.4
- Java SE 17.0.7
