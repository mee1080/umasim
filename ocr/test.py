import sys
import os
import cv2
import easyocr

# メインスクリプト（例: main.py）から関数をインポート
# ※もしメインのスクリプト名が異なる場合は、各自書き換えてください。
try:
    from analyze import extract_training_status
except ImportError:
    print("Error: main.py から extract_training_status をインポートできませんでした。")
    print("このスクリプトは main.py と同じディレクトリに配置してください。")
    sys.exit(1)

def main():
    # 引数のチェック（第1引数に画像パスが必要）
    if len(sys.argv) < 2:
        print("Usage: uv run test.py <画像ファイルのパス>")
        print("Example: uv run test.py WS000002.png")
        sys.exit(1)
        
    image_path = sys.argv[1]
    
    # ファイルの存在確認
    if not os.path.exists(image_path):
        print(f"Error: 指定されたファイルが見つかりません: {image_path}")
        sys.exit(1)
        
    print(f"[{image_path}] のデバッグ解析を開始します...")
    print("EasyOCRを初期化中...")
    reader = easyocr.Reader(['en'], gpu=False)
    
    # 解析実行（debug_mode=True で temp_xxx.png を生成）
    status_data = extract_training_status(image_path, reader, debug_mode=True)
    
    if status_data:
        print("\n--- [OCR 読み取り結果] ---")
        for key, val in status_data.items():
            print(f"{key}: +{val}")

if __name__ == "__main__":
    main()
