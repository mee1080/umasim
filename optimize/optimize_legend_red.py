import subprocess
import optuna

def objective(trial):

    count = 100000

    evaluateSpeed   = '1.0 2500'
    evaluateStamina = '1.0 2000'
    evaluatePower   = '1.0 2000'
    evaluateGuts    = '1.0 1800'
    evaluateWisdom  = '1.0 1700'
    evaluateSkillPt = '1.0 8000'

    speed = 100
    stamina = 100
    power = 100
    guts = 100
    wisdom = 70

    training = [
        trial.suggest_int ('training1', 100, 500, step=50),
        trial.suggest_int ('training2', 0, 400, step=50),
    ]
    hp = 20
    hpKeep = trial.suggest_int ('hpKeep', 0, 40, step=5)
    motivation = trial.suggest_int ('motivation', 800, 1200, step=100)
    risk = trial.suggest_int ('risk', 0, 30, step=5)

    relation = trial.suggest_int ('relation', 10, 80, step=10)

    friend = trial.suggest_int ('friend', 0, 500, step=50)
    friendCount = trial.suggest_int ('friendCount', 0, 500, step=50)
    supportCount = trial.suggest_int ('supportCount', 200, 600, step=50)
    guestCount = trial.suggest_int ('guestCount', 50, 400, step=50)

    forcedSupportCount = trial.suggest_int ('forcedSupportCount', -400, 0, step=50)
    supportBestFriendGauge = trial.suggest_int ('supportBestFriendGauge', 0, 200, step=25)
    forcedGuestCount = trial.suggest_int ('forcedGuestCount', 100, 400, step=50)

    cmd = f'java -Dfile.encoding=UTF-8 -jar ../cli/build/libs/cli.jar --data-dir ../data'\
          f' --count {count}'\
          f' --chara "[リアライズ・ルーン]スイープトウショウ" 5 5'\
          f' --support "[世界を変える眼差し]アーモンドアイ" 4'\
          f' --support "[Devilish Whispers]スティルインラブ" 4'\
          f' --support "[誘うは夢心地]ドリームジャーニー" 4'\
#          f' --support "[Cocoon]エアシャカール" 4'\
#          f' --support "[そして幕は上がる]ダンツフレーム" 4'\
          f' --support "[繋がれパレード・ノーツ♪]トランセンド" 4'\
          f' --support "[Take Them Down!]ナリタタイシン" 4' \
          f' --support "[導きの光]伝説の体現者" 4' \
          f' --factor STAMINA 3 --factor STAMINA 3 --factor STAMINA 3'\
          f' --factor STAMINA 3 --factor STAMINA 3 --factor STAMINA 3'\
          f' --evaluate SPEED {evaluateSpeed}'\
          f' --evaluate STAMINA {evaluateStamina}'\
          f' --evaluate POWER {evaluatePower}'\
          f' --evaluate GUTS {evaluateGuts}'\
          f' --evaluate WISDOM {evaluateWisdom}'\
          f' --evaluate SKILL {evaluateSkillPt}' \
          \
          f' --buff "トーク術,交渉術,素敵なハーモニー,極限の集中,絆が奏でるハーモニー,怪物チャンスマイル♪,絆が織りなす光,集いし理想,高潔なる魂,百折不撓,飽くなき挑戦心"' \
          \
          f' --speed {speed} --stamina {stamina} --power {power}' \
          f' --guts {guts} --wisdom {wisdom}' \
          \
          f' --training {training[0]} --training {training[1]}' \
          f' --hp {hp} --hp-keep {hpKeep} --motivation {motivation} --risk {risk}' \
          \
          f' --relation {relation} --outing-relation {relation}'\
          \
          f' --friend {friend} --friend-count {friendCount}'\
          f' --support-count {supportCount} --guest-count {guestCount}'\
          \
          f' --forced-support-count {forcedSupportCount}'\
          f' --support-best-friend-gauge {supportBestFriendGauge}'\
          f' --forced-guest-count {forcedGuestCount}'\
          f''

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

study = optuna.create_study(
    study_name='legend_red_s2h2w1_5',
    storage='sqlite:///optuna_study_legend.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=100000)
