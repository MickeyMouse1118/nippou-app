# 簡易日報アプリ

9月「改修(読む・直す)」フェーズで使う練習用アプリです。
Servlet(Java)+JSP+JDBC(MySQL)のみで作られています。フレームワーク(Spring等)は使っていません。

詳しい環境構築手順(JDK・Tomcat・MySQL・Eclipseのインストールなど)は、別途渡す「環境構築チェックリスト」を参照してください。
このREADMEでは、このアプリ固有のセットアップ手順と、取り組む課題をまとめています。

## 1. アプリのセットアップ手順

### 1-1. MySQLへのスキーマ投入

コマンドプロンプトでこのフォルダ(`09_9月改修対象アプリ`)に移動し、以下を実行してください。

```
mysql -u root -p --default-character-set=utf8mb4 < sql\01_schema.sql
mysql -u root -p --default-character-set=utf8mb4 < sql\02_seed.sql
```

rootパスワードの入力を求められるので入力してください。データベース・テーブル・アプリ用ユーザー(`nippou_user`)が作成され、サンプル日報10件が投入されます。

- `--default-character-set=utf8mb4` を付けないと、日本語(マルチバイト文字)が原因で `ERROR 1406: Data too long for column` が出ることがあります(文字コードの不一致で、実際の文字数より長く判定されてしまうため)。必ず付けて実行してください。

- `mysql` コマンドが「'mysql' は、内部コマンドまたは外部コマンド、操作可能なプログラムまたはバッチ ファイルとして認識されていません。」と出る場合、環境変数PATHが通っていません(MySQL Installerでは自動で通らないことがあります)。以下の手順で追加してください。
  1. スタートメニューで「環境変数」と検索し、「システム環境変数の編集」を開く
  2. 表示された画面で「環境変数(N)...」ボタンをクリック
  3. 下側の「システム環境変数」欄から `Path` を選択して「編集」をクリック
  4. 「新規」をクリックし、`C:\Program Files\MySQL\MySQL Server 8.0\bin` を追加(インストール先が違う場合はそのパスに合わせる)
  5. 「OK」→「OK」→「OK」で全ての画面を閉じる
  6. 開いているコマンドプロンプトを一度閉じて、新しく開き直す(閉じ直さないとPATHの変更が反映されません)
  7. 再度 `mysql --version` を実行し、バージョンが表示されればOK

### 1-2. MySQL Connector/Jの配置

1. 以下からjarファイルを直接ダウンロード(バージョンはページを開いた時点の最新版でOK)
   - Maven Central: https://central.sonatype.com/artifact/com.mysql/mysql-connector-j
   - (公式サイトからのzip版でも可: https://dev.mysql.com/downloads/connector/j/ )
2. `src/main/webapp/WEB-INF/` の中に `lib` という名前のフォルダを新規作成し、ダウンロードしたjarをそこに配置する

### 1-3. Eclipseでプロジェクトを作成

1. `ファイル → 新規 → 動的Webプロジェクト`
2. プロジェクト名を入力(例: `nippou-app`。これがそのままアクセスURLのコンテキスト名になります)
3. 「デフォルトの場所を使用」のチェックを**外し**、「参照」でこの `09_9月改修対象アプリ` フォルダを直接指定する(既存のソース一式をそのまま使うため)
4. ターゲット・ランタイム: 「Apache Tomcat v10.1」を選択
   - Tomcatは Pleiades All in One に同梱されていることがあります(例: `C:\pleiades\2026-06\tomcat\10`)。なければ公式サイト(tomcat.apache.org/download-10.cgi)から10.1系をダウンロード
   - 実行環境(JRE)は同梱されているJava17以降のいずれかでOK(Java21推奨)
5. 動的Webモジュールのバージョン: `6.0`
6. 「次へ」→ Javaソースフォルダが `src/main/java` になっているか確認(なっていなければ変更)
7. 「次へ」→ コンテンツ・ディレクトリーを `src/main/webapp` に変更し、「web.xml配置記述子の生成」のチェックを**外す**(既存のweb.xmlを上書きさせないため)
8. 「完了」
9. プロジェクト・エクスプローラーでJava/JSPファイルが正しく表示されない場合は、プロジェクトを右クリック→「更新」(F5)

### 1-4. サーバーへのデプロイと起動

1. 「サーバー」ビューでTomcat 10を右クリック→「追加および削除」
2. 作成したプロジェクトを「使用可能」から「構成済み」へ移動して「完了」
3. Tomcatを右クリック→「開始」(すでに起動中なら「再起動」)
4. ブラウザで `http://localhost:8080/(プロジェクト名)/` にアクセスする
   → 日報一覧が表示されれば成功

接続情報(DB名・ユーザー名・パスワード)は `src/main/java/com/example/nippou/dao/DBUtil.java` に直書きされています。
自分の環境に合わせて必要があれば書き換えてください。

### 1-5. 修正を反映させる方法(JSPとJavaの違い)

小改修課題を進める中で、「直したのに画面が変わらない」と困ったときはここを確認してください。ファイルの種類によって、反映のさせ方が違います。

**JSPファイル(`list.jsp`・`detail.jsp`・`form.jsp`など)を直した場合**

保存するだけでOKです。Tomcatを起動したままの状態で、ブラウザをリロード(再読み込み)すれば変更が反映されます。サーバーの再起動は不要です。

**Javaファイル(Servlet・DAO・`Report`クラスなど、`src/main/java`配下)を直した場合**

保存すると、Eclipseが自動でコンパイル(ビルド)してくれます(デフォルトで「プロジェクト→自動的にビルド」がONになっているため、特別な操作は不要)。
ただし、コンパイルされただけでは起動中のTomcatに変更が反映されないことがあります。「サーバー」ビューでTomcatを右クリック→「再起動」してから、ブラウザをリロードして確認してください。
(直したはずなのに動きが変わらない、というときはまずTomcatの再起動を試してみてください)

**SQL文を直した場合**

`ReportDao.java`の中の`ORDER BY`などのSQL文は、Javaのソースコード内の文字列として書かれています。そのため上の「Javaファイルを直した場合」と同じ扱いになり、直したらTomcatの再起動が必要です。
これに対して、`sql/01_schema.sql`や`sql/02_seed.sql`自体を直した場合は話が別です。これらはテーブルの作成・初期データ投入用のファイルなので、直しただけでは何も起きず、1-1の手順でもう一度MySQLに流し込む必要があります(`DROP TABLE`を含むため、既存データは消えてやり直しになる点に注意してください)。

## 2. 画面の一覧

- 日報一覧: `/reports`
- 日報詳細: `/reports/detail?id=1`
- 日報新規登録: `/reports/new`
- 日報編集: `/reports/edit?id=1`

## 3. 小改修課題

以下を上から順に、1つずつ「動かして確認→次へ」で進めてください。
それぞれ「対象ファイル」「今の状態」「進め方の例」「確認方法」を書いています。進め方は一例なので、書いてある通りにしなくても、自分なりのやり方で実装してもらって構いません。わからなければ遠慮なくチャットで質問してください。

### 課題1: 一覧画面のタイトル文言を変更する

- 対象ファイル: `list.jsp`
- 今の状態: 見出し(`<h1>`)が「日報一覧」になっている
- 進め方の例: `<h1>`の中身を、画面の内容が伝わる好きな文言に書き換える
- 確認方法: `/reports` にアクセスし、タイトルが変わっていることを確認する

### 課題2: 日報詳細画面に「所感」欄の表示を追加する

- 対象ファイル: `detail.jsp`
- 今の状態: DB(`remarks`カラム)・DAO(`ReportDao`)・`Report`クラス(`getRemarks()`)には所感を取得する仕組みが既にあるが、詳細画面の表には表示されていない
- 進め方の例: 表の中の「作業内容」の行などを参考に、「所感」の行を1つ追加し、`report.getRemarks()`を表示する。所感が未入力(null)のデータもあるので、その場合の見え方(空欄のまま/「(なし)」と表示、など)は自分で決めてよい
- 確認方法: `/reports/detail?id=1` などにアクセスし、所感が表示されることを確認する(idによって所感ありのデータ・なしのデータ両方を確認できるとなお良い)

### 課題3: 一覧画面に「全◯件」のような件数表示を追加する

- 対象ファイル: `list.jsp`
- 今の状態: 何件登録されているかの表示がない
- 進め方の例: `reportList`は`List`なので`.size()`で件数が取得できる。見出しの下や表の上あたりに「全10件」のように表示する
- 確認方法: `/reports` にアクセスし、実際の登録件数と表示件数が一致していることを確認する

### 課題4: 対象日の表示形式を `yyyy/MM/dd` に統一する

- 対象ファイル: `list.jsp`、`detail.jsp`(対象日を表示している箇所すべて)
- 今の状態: `Report`クラスの`reportDate`は`yyyy-MM-dd`形式の文字列(例: `2026-09-03`)としてそのまま保持されており、画面にもハイフン区切りで表示されている
- 進め方の例: まずは文字列の`replace("-", "/")`で置き換えるだけでも対応できる。余裕があれば`java.time.LocalDate`等を使った変換にも挑戦してみるとよい
- 確認方法: 一覧・詳細の両方の対象日が `2026/09/03` のようなスラッシュ区切りで表示されることを確認する

### 課題5: 登録フォームの送信ボタンのラベル文言を変更する

- 対象ファイル: `form.jsp`
- 今の状態: 新規登録・編集どちらの画面でも送信ボタンが「登録」という文言(`<input type="submit" value="登録">`)になっている
- 進め方の例: 好きな文言に変更する。余裕があれば`mode`(`"new"`/`"edit"`)によって「登録する」「更新する」のように出し分けてみてもよい(必須ではない)
- 確認方法: `/reports/new` と `/reports/edit?id=1` の両方でボタンの文言を確認する

### 課題6: 作業内容が空欄のまま登録しようとした場合に、エラーメッセージを表示する

- 対象ファイル: `ReportCreateServlet.java`(`doPost`)、`form.jsp`
- 今の状態: 作業内容(`workContent`)が空でも、そのままDBに登録できてしまう
- 進め方の例:
  1. `doPost`内で、`reportDao.insert()`を呼ぶ前に`workContent`が`null`または空文字(`trim()`後)かどうかをチェックする
  2. NGの場合はDB登録処理を呼ばず、エラーメッセージをリクエストスコープ(`request.setAttribute`)に積んで`form.jsp`にフォワードする(このとき`mode`と、すでに入力済みの値も一緒に積み直すと、入力し直す手間が減る)
  3. `form.jsp`側で、エラーメッセージがあれば表示する処理を追加する
- 確認方法: 作業内容を空欄のまま登録ボタンを押し、エラーメッセージが表示されること・DBに登録されていないことの両方を確認する

### 課題7: 一覧画面の「新規登録」導線を、テキストリンクから目立つボタンに変更する

- 対象ファイル: `list.jsp`
- 今の状態: `<a href="...">新規登録</a>` という文字だけのリンクになっている
- 進め方の例: `<button>`タグにする、リンクをCSSでボタン風に装飾する、など見た目でクリックしたくなる形にする(デザインに正解はない)
- 確認方法: `/reports` にアクセスし、新規登録の導線がボタンらしい見た目になっていることを確認する

### 課題8: 動作がおかしい箇所を自分で見つけて直す

**動作がおかしい箇所が全部で3つあります。** 以下のヒントを参考に、コードを読んで原因を特定し、直してください。原因の探し方までがヒントで、直し方は書いていないので、原因が分かったら修正方法は自分で考えてみてください。

- ヒント1(登録・編集まわり): 対象日を**空欄のまま**、または**未来の日付**を入れて登録・編集してみてください。それでも登録できてしまわないか確認しましょう。
- ヒント2(一覧の並び順): 一覧画面に表示される日報を、対象日の順序に注目して見てください。「対象日の新しい順」に並んでいるでしょうか?
- ヒント3(日本語の扱い): 作業内容や所感に日本語(ひらがな・漢字)を入力して新規登録・編集し、一覧画面や詳細画面で保存された内容がどう表示されるか確認してみてください。
- 進め方の例: 上記のヒントの操作を実際に試す → 「あれ?」と思う挙動が起きたら、関係していそうなServlet/JSP/SQLを読む → 「ここが原因では」と仮説を立てて検証する、という流れで進めるとよい
- 確認方法: 見つけた不具合について「どういう操作をすると」「何が起きるか(期待される動きと実際の動きの違い)」をチャットで説明できる状態にしてから直す

### 課題9: 日報を削除する機能を追加する

これまでの課題は既存のファイルを直すものでしたが、この課題だけは「新しいファイル(Servlet)を1つ作る」という初めての作業が入ります。難易度が上がるので、ステップを細かく分けて説明します。1つずつ確認しながら進めてください。

**今の状態**

新規登録・詳細表示・編集(更新)はできますが、一度登録した日報を削除する機能がありません。「削除」を実現するには、以下の3つのファイルに手を入れる必要があります。

- `ReportDao.java`: DB(MySQL)とやり取りする役目のクラス。ここに「idを指定して1件消す」処理を追加します。
- `ReportDeleteServlet.java`(今回新規作成): 画面からの「削除して」というリクエストを受け取り、`ReportDao`に削除を依頼する橋渡し役です。今のアプリには「削除」を受け取る係がまだいないので、新しく作ります。
- `detail.jsp`: 削除ボタンを置く画面です。

**ステップ1: `ReportDao.java`に削除用メソッドを追加する**

`ReportDao.java`を開き、`update`メソッドのすぐ下あたりに、以下のメソッドをそのまま追加してください。`update`メソッドと形はほとんど同じで、SQL文が`DELETE`に変わっただけです。

```java
public void delete(int id) throws SQLException {
    String sql = "DELETE FROM reports WHERE id = ?";

    try (Connection conn = DBUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}
```

- `DELETE FROM reports WHERE id = ?`: 「idが一致する行を1件消す」というSQL文です。`?`の部分に、実際のidが後から入ります。
- `stmt.setInt(1, id)`: SQL文の1個目の`?`に、引数で受け取った`id`をあてはめています。
- `stmt.executeUpdate()`: `insert`や`update`のときと同じ書き方です。DELETEもINSERT/UPDATEと同じ「executeUpdate」で実行します(SELECTのときだけ`executeQuery`でした)。

**ステップ2: `ReportDeleteServlet.java`を新規作成する**

1. Eclipseのプロジェクト・エクスプローラーで、`ReportUpdateServlet.java`が入っているフォルダ(`src/main/java/com/example/nippou/servlet`)を右クリックします。
2. 「新規」→「クラス」を選び、名前(名前ボックス)に`ReportDeleteServlet`と入力して「完了」を押します。
3. できた空のファイルの中身を、以下のコードで丸ごと置き換えてください。

```java
package com.example.nippou.servlet;

import com.example.nippou.dao.ReportDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 日報削除処理。
 * POST /reports/delete
 */
@WebServlet("/reports/delete")
public class ReportDeleteServlet extends HttpServlet {

    private final ReportDao reportDao = new ReportDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        try {
            reportDao.delete(id);
        } catch (SQLException e) {
            throw new ServletException("日報の削除に失敗しました", e);
        }

        response.sendRedirect(request.getContextPath() + "/reports");
    }
}
```

このコードが何をしているか、上から順に説明します。

- `@WebServlet("/reports/delete")`: 「`/reports/delete`宛てのリクエストが来たら、このクラスが担当します」という目印です。`ReportUpdateServlet.java`の`@WebServlet("/reports/update")`と同じ仕組みです。
- `int id = Integer.parseInt(request.getParameter("id"));`: 画面から送られてきた`id`(文字列)を、数値(int)に変換して受け取っています。`ReportUpdateServlet.java`にも同じ行があるので見比べてみてください。
- `reportDao.delete(id);`: ステップ1で作った削除メソッドを呼び出しています。
- `response.sendRedirect(request.getContextPath() + "/reports");`: 削除が終わったら、一覧画面(`/reports`)に画面を切り替えています。

**ステップ3: `detail.jsp`に削除ボタンを追加する**

`detail.jsp`を開き、「編集」「一覧へ戻る」のリンクがある`<p>`タグの近くに、以下のコードを追加してください。

```jsp
<form method="post" action="<%= request.getContextPath() %>/reports/delete"
      onsubmit="return confirm('本当に削除しますか?');">
    <input type="hidden" name="id" value="<%= report.getId() %>">
    <input type="submit" value="削除">
</form>
```

なぜ`<a>`タグのリンクではなく、あえて`<form>`(フォーム)にしているのか、理由を説明します。

- 削除は一度やると元に戻せない操作です。`<a href="...">`のようなリンク(GETリクエスト)は、ブラウザが先読みしたり、間違ってクリックしたりするだけで実行されてしまう可能性があります。ボタンを押して送信する`<form method="post">`(POSTリクエスト)にすることで、「本当に送信していいか」というワンクッションを置いています。
- `onsubmit="return confirm('本当に削除しますか?');"`: ボタンを押した瞬間に確認ポップアップを出し、「キャンセル」を押せば送信を取り消せるようにしています。これが無いと、ボタンを1回押しただけで即削除されてしまい危険です。
- `<input type="hidden" name="id" value="<%= report.getId() %>">`: 画面には見えない形で、「どのidの日報を消すか」をServlet側に渡しています。

**ステップ4: 動作確認**

1. 新しいJavaファイル(`ReportDeleteServlet.java`)を作ったので、Eclipseでプロジェクトを右クリック→「更新」(F5)を行い、その後Tomcatを右クリック→「再起動」してください(Javaファイルの変更なので、1-5節の通り再起動が必要です)。
2. ブラウザで日報の詳細画面(`/reports/detail?id=1`など)を開き、「削除」ボタンが表示されていることを確認します。
3. 削除ボタンを押すと確認ダイアログが出るので、一度「キャンセル」を押して、何も起きない(消えない)ことを確認します。
4. もう一度削除ボタンを押し、今度は「OK」を押します。一覧画面(`/reports`)に戻り、今削除したデータが表示されなくなっていることを確認します。
5. 余裕があれば、MySQLに直接ログインして`SELECT * FROM reports;`を実行し、DB上からも本当にその行が消えていることを確認してみてください。

**発展(必須ではない)**

誤操作で消してしまうと元のデータには戻せません。データを本当には消さずに、`is_deleted`のようなフラグ列を追加して「一覧には表示しないだけ」にする「論理削除」という考え方もあります。余裕があれば調べて挑戦してみてください。

## 4. Git/GitHubでの進め方

このアプリはGit/GitHubで管理しながら進めます。8月のProgate「Git」コースで覚えたコマンドを、ここで実際の課題に使ってみましょう。

1. GitHubアカウントを作成する(まだの場合)
2. このフォルダ(`09_9月改修対象アプリ`)で `git init` してリポジトリを初期化する
3. 最初の状態を `git add .` → `git commit -m "初期状態"` でコミットする
4. GitHubで新しいリポジトリ(例: `nippou-app`)を作成し、`git remote add origin <URL>` → `git push` で最初のコミットをpushする
5. 小改修課題は**1課題ごとに1コミット**が基本(慣れてきたらブランチを切ってPull Requestを作る形にも挑戦してみてください)
   - コミットメッセージの例: `一覧画面のタイトル文言を変更`
6. 各課題のコミット後、`git push` でGitHubに反映する

Pull Requestでのレビューをやってみたい場合は、1on1で相談してください。おっくんがレビューしてマージします。
`.gitignore` は用意済みです(Eclipseの設定ファイルやビルド成果物、`WEB-INF/lib`配下のjarは対象外にしています)。

## 5. 提出物

各課題を直したら、動作確認のスクリーンショットとGitHubのコミット履歴(またはリポジトリのURL)をチャットに提出してください。
