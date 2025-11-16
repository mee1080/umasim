import subprocess
import optuna

def objective(trial):

    count = 100000

    status = 10
    skillPt = 100
    hp = trial.suggest_int ('hp', 2, 20, step=2)
    motivation = 100

    relation = trial.suggest_int ('relation', 1000, 2000, step=100)
    risk = trial.suggest_int ('risk', 100, 150, step=5)
    keepHp = trial.suggest_int ('keepHp', 80, 100, step=2)
    dig = trial.suggest_int ('dig', 100, 1000, step=50)

    cmd = f'java -Dfile.encoding=UTF-8 -jar ../cli/build/libs/cli.jar --data-dir ../data'\
          f' --count {count}'\
          f' --chara "[リアライズ・ルーン]スイープトウショウ" 5 5'\
          f' --support "[世界を変える眼差し]アーモンドアイ" 4'\
          f' --support "[Unveiled Dream]ラインクラフト" 4'\
          f' --support "[白き稲妻の如く]タマモクロス" 4'\
          f' --support "[繋がれパレード・ノーツ♪]トランセンド" 4'\
          f' --support "[Take Them Down!]ナリタタイシン" 4' \
          f' --support "[ゆるり、ゆこま旅館]保科健子" 4' \
          f' --factor SPEED 3 --factor SPEED 3 --factor SPEED 3'\
          f' --factor SPEED 3 --factor POWER 3 --factor POWER 3'\
          \
          f' --status {status} --skill-pt {skillPt} --hp {hp} --motivation {motivation}' \
          f' --relation {relation} --risk {risk} --keep-hp {keepHp} --dig {dig}' \
          f''

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

study = optuna.create_study(
    study_name='onsen_s2p1g1w1_2',
    storage='sqlite:///optuna_study_onsen.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=100000)
