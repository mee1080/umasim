ウマ娘レースエミュレータ移植版

ウマ娘のレースのシミュレーションを行います。

■使用方法

デスクトップ版：umasim.exe から起動します。

MCPサーバ版：以下のような設定を行います。
    "mcp": {
        "servers": {
            "umasim": {
                "command": "(展開したパス)\\umasim.exe",
                "args": ["mcp"]
            },
        }
    }
　対応機能
　・simulate_uma_race：シミュレーションの実行
　　（直近のチャンミorLOHのみ対応です）
　・get_uma_race_skill_data：スキル情報の取得


■アップデート

新しいリリースファイルを解凍した後、古いバージョンから、settings.confをコピーしてください。


■アンインストール

レジストリ等は使用していませんので、
プログラムのあるフォルダごと削除してください。


■ご意見、ご要望、バグ報告など

X(Twitter) @mee10801 に、DMまたはリプライをお願いします。

※ご要望にお応えできない可能性もありますので、ご了承ください。


■使用している計算式とデータについて

インターネット上で公開されているもの、
および自身で調査したものを使用しております。
妥当性については、保証いたしません。


■免責事項

このプログラムによって生じた損害について、作者は責任を負いません。


■GitHubリポジトリ

https://github.com/mee1080/umasim


■著作権表示

Copyright (c) 2025 mee1080
