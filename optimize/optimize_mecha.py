import subprocess
import optuna

def objective(trial):

    count = 10000

    evaluateSpeed   = '1.1 2200'
    evaluateStamina = '1.2 2000'
    evaluatePower   = '1.1 1700'
    evaluateGuts    = '0.9 1250'
    evaluateWisdom  = '1.0 1250'
    evaluateSkillPt = '1.2 6000'

    speed = [
        trial.suggest_int ('speed1', 60, 200, step=20),
        trial.suggest_int ('speed2', 60, 200, step=20),
        trial.suggest_int ('speed3', 60, 200, step=20),
        trial.suggest_int ('speed4', 60, 200, step=20),
        trial.suggest_int ('speed5', 60, 200, step=20),
    ]
    stamina = [
        trial.suggest_int ('stamina1', 60, 200, step=20),
        trial.suggest_int ('stamina2', 60, 200, step=20),
        trial.suggest_int ('stamina3', 60, 200, step=20),
        trial.suggest_int ('stamina4', 60, 200, step=20),
        trial.suggest_int ('stamina5', 60, 200, step=20),
    ]
    power = [
        trial.suggest_int ('power1', 60, 200, step=20),
        trial.suggest_int ('power2', 60, 200, step=20),
        trial.suggest_int ('power3', 60, 200, step=20),
        trial.suggest_int ('power4', 60, 200, step=20),
        trial.suggest_int ('power5', 60, 200, step=20),
    ]
    guts = [
        trial.suggest_int ('guts1', 60, 200, step=20),
        trial.suggest_int ('guts2', 60, 200, step=20),
        trial.suggest_int ('guts3', 60, 200, step=20),
        trial.suggest_int ('guts4', 60, 200, step=20),
        trial.suggest_int ('guts5', 60, 200, step=20),
    ]
    wisdom = [
        trial.suggest_int ('wisdom1', 60, 200, step=20),
        trial.suggest_int ('wisdom2', 60, 200, step=20),
        trial.suggest_int ('wisdom3', 60, 200, step=20),
        trial.suggest_int ('wisdom4', 60, 200, step=20),
        trial.suggest_int ('wisdom5', 60, 200, step=20),
    ]
    skillPt = [
        trial.suggest_int ('skillPt1', 60, 200, step=20),
        trial.suggest_int ('skillPt2', 60, 200, step=20),
        trial.suggest_int ('skillPt3', 60, 200, step=20),
        trial.suggest_int ('skillPt4', 60, 200, step=20),
        trial.suggest_int ('skillPt5', 60, 200, step=20),
    ]
    hp = [
        trial.suggest_int ('hp1', 20, 100, step=20),
        trial.suggest_int ('hp2', 20, 100, step=20),
        trial.suggest_int ('hp3', 20, 100, step=20),
        trial.suggest_int ('hp4', 20, 100, step=20),
        trial.suggest_int ('hp5', 20, 100, step=20),
    ]
    motivation = 3000
    relation = trial.suggest_int ('relation', 600, 1400, step=200)
    hpKeep = [
        trial.suggest_int ('hpKeep1', 100, 500, step=100),
        trial.suggest_int ('hpKeep2', 100, 500, step=100),
        trial.suggest_int ('hpKeep3', 100, 500, step=100),
        trial.suggest_int ('hpKeep4', 100, 500, step=100),
        trial.suggest_int ('hpKeep5', 100, 500, step=100),
    ]
    risk = [
        trial.suggest_int ('risk1', 100, 500, step=100),
        trial.suggest_int ('risk2', 100, 500, step=100),
        trial.suggest_int ('risk3', 100, 500, step=100),
        trial.suggest_int ('risk4', 100, 500, step=100),
        trial.suggest_int ('risk5', 100, 500, step=100),
    ]
    learningLevel = [
        trial.suggest_int ('learningLevel1', 40, 120, step=10),
        trial.suggest_int ('learningLevel2', 40, 120, step=10),
        trial.suggest_int ('learningLevel3', 40, 120, step=10),
        trial.suggest_int ('learningLevel4', 40, 120, step=10),
        trial.suggest_int ('learningLevel5', 40, 120, step=10),
    ]
    overdriveGauge = [
        trial.suggest_int ('overdriveGauge1', 1000, 5000, step=500),
        trial.suggest_int ('overdriveGauge2', 1000, 5000, step=500),
        trial.suggest_int ('overdriveGauge3', 1000, 5000, step=500),
        trial.suggest_int ('overdriveGauge4', 1000, 5000, step=500),
        trial.suggest_int ('overdriveGauge5', 1000, 5000, step=500),
    ]

    cmd = f'java -Dfile.encoding=UTF-8 -jar ../cli/build/libs/cli.jar --data-dir ../data'\
          f' --count {count} --scenario MECHA'\
          f' --chara "[リアライズ・ルーン]スイープトウショウ" 5 5'\
          f' --support "[咆哮のアポヤンド]ナリタブライアン" 4'\
          f' --support "[アルストロメリアの夢]ヴィブロス" 4'\
          f' --support "[Cocoon]エアシャカール" 4'\
          f' --support "[そして幕は上がる]ダンツフレーム" 4'\
          f' --support "[冬溶かす熾火]メジロラモーヌ" 4'\
          f' --support "[Take Them Down!]ナリタタイシン" 4'\
          f' --factor SPEED 3 --factor SPEED 3 --factor SPEED 3'\
          f' --factor SPEED 3 --factor SPEED 3 --factor SPEED 3'\
          f' --evaluate SPEED {evaluateSpeed}'\
          f' --evaluate STAMINA {evaluateStamina}'\
          f' --evaluate POWER {evaluatePower}'\
          f' --evaluate GUTS {evaluateGuts}'\
          f' --evaluate WISDOM {evaluateWisdom}'\
          f' --evaluate SKILL {evaluateSkillPt}'\
          f' --relation {relation} --outing-relation {relation} --motivation {motivation}'\
          \
          f' --speed {speed[0]} --stamina {stamina[0]} --power {power[0]}'\
          f' --guts {guts[0]} --wisdom {wisdom[0]} --skill-pt {skillPt[0]}'\
          f' --hp {hp[0]} --hp-keep {hpKeep[0]} --risk {risk[0]}'\
          f' --mecha-learning-level {learningLevel[0]} --mecha-overdrive-gauge {overdriveGauge[0]}'\
          \
          f' --speed {speed[1]} --stamina {stamina[1]} --power {power[1]}'\
          f' --guts {guts[1]} --wisdom {wisdom[1]} --skill-pt {skillPt[1]}'\
          f' --hp {hp[1]} --hp-keep {hpKeep[1]} --risk {risk[1]}'\
          f' --mecha-learning-level {learningLevel[1]} --mecha-overdrive-gauge {overdriveGauge[1]}'\
          \
          f' --speed {speed[2]} --stamina {stamina[2]} --power {power[2]}'\
          f' --guts {guts[2]} --wisdom {wisdom[2]} --skill-pt {skillPt[2]}'\
          f' --hp {hp[2]} --hp-keep {hpKeep[2]} --risk {risk[2]}'\
          f' --mecha-learning-level {learningLevel[2]} --mecha-overdrive-gauge {overdriveGauge[2]}'\
          \
          f' --speed {speed[3]} --stamina {stamina[3]} --power {power[3]}'\
          f' --guts {guts[3]} --wisdom {wisdom[3]} --skill-pt {skillPt[3]}'\
          f' --hp {hp[3]} --hp-keep {hpKeep[3]} --risk {risk[3]}'\
          f' --mecha-learning-level {learningLevel[3]} --mecha-overdrive-gauge {overdriveGauge[3]}'\
          \
          f' --speed {speed[4]} --stamina {stamina[4]} --power {power[4]}'\
          f' --guts {guts[4]} --wisdom {wisdom[4]} --skill-pt {skillPt[4]}'\
          f' --hp {hp[4]} --hp-keep {hpKeep[4]} --risk {risk[4]}'\
          f' --mecha-learning-level {learningLevel[4]} --mecha-overdrive-gauge {overdriveGauge[4]}'\
          f''

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

study = optuna.create_study(
    study_name='mecha_s2h2p1w1_1',
    storage='sqlite:///optuna_study_mecha.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=10000)
