import cv2
import easyocr
import re
import glob
import os

def extract_training_status(image_path, reader, debug_mode=True):
    # 画像の読み込み
    img = cv2.imread(image_path)
    if img is None:
        print(f"Error: 画像ファイル {image_path} が見つかりません。")
        return None
    
    h, w, _ = img.shape
    
    # 割合によるROIの定義（画像サイズに合わせて調整してください）
    roi_definitions = {
        "speed": [int(h * 0.63), int(w * 0.05), int(h * 0.66), int(w * 0.20)],
        "stamina": [int(h * 0.63), int(w * 0.20), int(h * 0.66), int(w * 0.36)],
        "power":   [int(h * 0.63), int(w * 0.36), int(h * 0.66), int(w * 0.51)],
        "guts":     [int(h * 0.63), int(w * 0.51), int(h * 0.66), int(w * 0.66)],
        "wisdom":     [int(h * 0.63), int(w * 0.66), int(h * 0.66), int(w * 0.81)],
        "skillPt": [int(h * 0.63), int(w * 0.81), int(h * 0.66), int(w * 0.96)],

        "speed2": [int(h * 0.60), int(w * 0.05), int(h * 0.63), int(w * 0.20)],
        "stamina2": [int(h * 0.60), int(w * 0.20), int(h * 0.63), int(w * 0.36)],
        "power2":   [int(h * 0.60), int(w * 0.36), int(h * 0.63), int(w * 0.51)],
        "guts2":     [int(h * 0.60), int(w * 0.51), int(h * 0.63), int(w * 0.66)],
        "wisdom2":     [int(h * 0.60), int(w * 0.66), int(h * 0.63), int(w * 0.81)],
        "skillPt2": [int(h * 0.60), int(w * 0.81), int(h * 0.63), int(w * 0.96)],
    }
    
    results = {}
    base_filename = os.path.basename(image_path)
    
    for stat_name, coords in roi_definitions.items():
        ymin, xmin, ymax, xmax = coords
        cropped = img[ymin:ymax, xmin:xmax]
        
        # OCR用の前処理（グレースケール化・3倍に拡大）
        gray = cv2.cvtColor(cropped, cv2.COLOR_BGR2GRAY)
        resized = cv2.resize(gray, (0, 0), fx=3, fy=3, interpolation=cv2.INTER_CUBIC)
        
        # デバッグ画像保存
        if debug_mode:
            name_without_ext = os.path.splitext(base_filename)[0]
            debug_filename = f"temp_{name_without_ext}_{stat_name}.png"
            cv2.imwrite(debug_filename, resized)
        
        # OCR実行
        ocr_result = reader.readtext(resized, detail=0)
        text = "".join(ocr_result)
        
        if debug_mode:
            print(f"read {stat_name} {text}")
        
        # 2文字目以降のみを採用
        if len(text) >= 2:
            text = text[1:]
        
        numbers = re.findall(r'\d+', text)
        results[stat_name] = int(numbers[0]) if numbers else 0

    return results

# --- メイン処理 ---
if __name__ == "__main__":
    print("EasyOCRを初期化中...")
    reader = easyocr.Reader(['en'], gpu=True)
    
    # "WS*.png" のファイルを全取得
    image_files = sorted(glob.glob("WS*.png"))
    
    if not image_files:
        print("カレントディレクトリに 'WS*.png' ファイルが見つかりませんでした。")
    else:
        output_file = "result.txt"
        print(f"{len(image_files)} 件の画像ファイルを解析し、{output_file} に書き出します...")
        
        # ファイルを書き込みモードで開く
        with open(output_file, "w", encoding="utf-8") as f:
            for image_file in image_files:
                filename = os.path.basename(image_file)
                print(f"{filename} を処理中")
                status_data = extract_training_status(image_file, reader, debug_mode=False)
                
                if status_data:
                    # テキストファイルにフォーマット通り書き出し
                    type = "SPEED"
                    if status_data['wisdom'] > 0:
                        type = "WISDOM"
                    elif status_data['stamina'] > 0:
                        if status_data['guts'] > 0:
                            type = "STAMINA"
                        else:
                            type = "POWER"
                    elif status_data['guts'] > 0:
                        type = "GUTS"
                    f.write(f"        // {filename}\n")
                    f.write("        testTraining(\n")
                    f.write(f"            baseCalcInfo, StatusType.{type}, 1, 0,\n")
                    f.write(f"            base = Status({status_data['speed']}, {status_data['stamina']}, {status_data['power']}, {status_data['guts']}, {status_data['wisdom']}, {status_data['skillPt']}),\n")
                    f.write(f"            scenario = Status({status_data['speed2']}, {status_data['stamina2']}, {status_data['power2']}, {status_data['guts2']}, {status_data['wisdom2']}, {status_data['skillPt2']}),\n")
                    f.write("        )\n\n")
                    
        print(f"完了しました！ {output_file} を確認してください。")
