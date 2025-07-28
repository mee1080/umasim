import subprocess
import optuna

def objective(trial):

    count = 100000

    status = 10
    skillPt = 100
    hp = 5
    motivation = 10000
    training = 100
    race = trial.suggest_int ('race', 50, 100, step=10)
    relation = trial.suggest_int ('relation', 1000, 3000, step=200)
    risk = trial.suggest_int ('risk', 100, 200, step=20)
    ignoreFailureRate = 0
    sleepHp = trial.suggest_int ('sleepHp', 40, 70, step=5)
    restPointBuffer = trial.suggest_int ('restPointBuffer', 20, 70, step=10)
    keepRestPoint = trial.suggest_int ('keepRestPoint', 200, 300, step=20)
    baseIslandTraining = trial.suggest_int ('baseIslandTraining', 3000, 7000, step=500)

    cmd = f'java -Dfile.encoding=UTF-8 -jar ../cli/build/libs/cli.jar --data-dir ../data'\
          f' --count {count}'\
          f' --chara "[リアライズ・ルーン]スイープトウショウ" 5 5'\
          f' --support "[世界を変える眼差し]アーモンドアイ" 4'\
          f' --support "[Cocoon]エアシャカール" 4'\
          f' --support "[白き稲妻の如く]タマモクロス" 4'\
          f' --support "[只、君臨す。]オルフェーヴル" 4'\
          f' --support "[Take Them Down!]ナリタタイシン" 4' \
          f' --support "[本能は吼えているか！？]タッカーブライン" 4' \
          f' --factor SPEED 3 --factor SPEED 3 --factor SPEED 3'\
          f' --factor SPEED 3 --factor SPEED 3 --factor SPEED 3'\
          \
          f' --status {status} --skill-pt {skillPt} --hp {hp} --motivation {motivation}' \
          f' --training {training} --race {race} --relation {relation} --risk {risk}' \
          f' --ignore-failure-rate {ignoreFailureRate} --sleep-hp {sleepHp}' \
          f' --rest-point-buffer {restPointBuffer} --keep-rest-point {keepRestPoint}' \
          f' --base-island-training {baseIslandTraining}' \
          f''

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

study = optuna.create_study(
    study_name='mujinto_s1h1p1g1w1_2',
    storage='sqlite:///optuna_study_mujinto.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=100000)
