-- 動作確認用の初期データ
-- report_date(対象日)とcreated_at(登録日時)の順序をあえてずらしてあります。
-- 一覧が「登録日時の古い順」で表示されると、日付が入り乱れて見えるはずです。
-- (これは仕込みバグ No.2 の確認用です。詳細は 講師用_仕込みバグ一覧.md 参照)

USE nippou_db;

INSERT INTO reports (reporter_name, report_date, work_content, remarks, created_at, updated_at) VALUES
('西村', '2026-09-03', '環境構築(JDK・Tomcat・MySQLのインストール)を行った。', '思ったよりスムーズに進んだ。', NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 10 DAY),
('西村', '2026-09-01', 'キックオフ。既存アプリの動作確認と全体構成の把握。', NULL, NOW() - INTERVAL 9 DAY, NOW() - INTERVAL 9 DAY),
('西村', '2026-09-05', 'コードリーディング。Servlet→JSPの流れを図に書き出した。', '処理の流れが繋がって見えてきた。', NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 8 DAY),
('西村', '2026-09-02', 'MySQLの接続確認、サンプルデータの中身を確認。', NULL, NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 7 DAY),
('西村', '2026-09-08', '小改修①(一覧タイトルの文言変更)に着手。', NULL, NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 6 DAY),
('西村', '2026-09-04', 'DBスキーマとDAOの対応関係を確認。PreparedStatementの書き方を復習。', '写経しながら理解した。', NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY),
('西村', '2026-09-10', '小改修②(詳細画面に所感欄を追加)完了。', NULL, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY),
('西村', '2026-09-06', '一覧画面のソート順が想定と違うことに気づいた。原因を調査中。', '要質問リストに追加。', NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY),
('西村', '2026-09-09', '日本語を登録すると文字化けする不具合を発見。', NULL, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY),
('西村', '2026-09-07', '1on1で不具合の調査方針を相談。', 'ログの読み方を教わった。', NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY);
