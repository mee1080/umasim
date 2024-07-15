import subprocess
import optuna

def objective(trial):

    evaluateSpeed = '1.1 2400'
    evaluateStamina = '1.2 550'
    evaluatePower = '1.1 2200'
    evaluateGuts = '1.1 2200'
    evaluateWisdom = '1.1 1400'
    evaluateSkillPt = '0.8 5000'

    speed = 1.0
    stamina = 1.0
    power = 1.1
    guts = 0.6
    wisdom = trial.suggest_float ('wisdom', 1.0, 1.5, step=0.1)
    skillPt = trial.suggest_float ('skillPt', 0.5, 1.0, step=0.1)

    hp1 = trial.suggest_float ('hp1', 0.2, 1.0, step=0.1)
    hp2 = trial.suggest_float ('hp2', 0.2, 1.0, step=0.1)
    hp3 = trial.suggest_float ('hp3', 0.2, 1.0, step=0.1)
    motivation = trial.suggest_float ('motivation', 5.0, 15.0, step=2.0)
    relation = trial.suggest_float ('relation', 5.0, 20.0, step=2.5)
    outingRelation = trial.suggest_float ('outingRelation', 5.0, 50.0, step=5.0)
    hpKeep1 = trial.suggest_float ('hpKeep1', 0.0, 0.5, step=0.1)
    hpKeep2 = trial.suggest_float ('hpKeep2', 0.0, 0.5, step=0.1)
    hpKeep3 = trial.suggest_float ('hpKeep3', 0.0, 0.5, step=0.1)
    risk1 = trial.suggest_float ('risk1', 1.0, 5.0, step=1.0)
    risk2 = trial.suggest_float ('risk2', 1.0, 5.0, step=1.0)
    risk3 = trial.suggest_float ('risk3', 1.0, 5.0, step=1.0)

    stamp1 = trial.suggest_float ('stamp1', 0.0, 5.0, step=0.5)
    stamp2 = trial.suggest_float ('stamp2', 0.0, 5.0, step=0.5)
    stamp3 = trial.suggest_float ('stamp3', 0.0, 5.0, step=0.5)
    fullPower1 = trial.suggest_float ('fullPower1', 10.0, 50.0, step=5.0)
    fullPower2 = trial.suggest_float ('fullPower2', 10.0, 50.0, step=5.0)
    fullPower3 = trial.suggest_float ('fullPower3', 10.0, 50.0, step=5.0)
    cookThreshold1 = trial.suggest_float ('cookThreshold1', 20.0, 100.0, step=5.0)
    cookThreshold2 = trial.suggest_float ('cookThreshold2', 20.0, 100.0, step=5.0)
    cookThreshold3 = trial.suggest_float ('cookThreshold3', 20.0, 100.0, step=5.0)
    cookPtLimit1 = trial.suggest_int ('cookPtLimit1', 1500, 4000, step=500)
    cookPtLimit2 = trial.suggest_int ('cookPtLimit2', 6500, 9000, step=500)
    cookPtRequired1 = trial.suggest_int ('cookPtRequired1', 1500, 3000, step=500)
    cookPtRequired2 = trial.suggest_int ('cookPtRequired2', 2500, 7000, step=500)
    cookPtRequired3 = trial.suggest_int ('cookPtRequired3', 5000, 10000, step=1000)

    cmd = f'java -Dfile.encoding=UTF-8 -jar ../cli/build/libs/cli.jar --data-dir ../data'\
          f' --count 50000 --scenario COOK'\
          f' --chara "[リアライズ・ルーン]スイープトウショウ" 5 5'\
          f' --support "[アルストロメリアの夢]ヴィブロス" 4'\
          f' --support "[朝焼け苺の畑にて]ニシノフラワー" 4'\
          f' --support "[うらら～な休日]ハルウララ" 4'\
          f' --support "[只、君臨す。]オルフェーヴル" 4'\
          f' --support "[Take Them Down!]ナリタタイシン" 4'\
          f' --support "[謹製ッ！特大夢にんじん！]秋川理事長" 4'\
          f' --factor SPEED 3 --factor SPEED 3 --factor SPEED 3'\
          f' --factor SPEED 3 --factor SPEED 3 --factor SPEED 3'\
          f' --evaluate SPEED {evaluateSpeed}'\
          f' --evaluate STAMINA {evaluateStamina}'\
          f' --evaluate POWER {evaluatePower}'\
          f' --evaluate GUTS {evaluateGuts}'\
          f' --evaluate WISDOM {evaluateWisdom}'\
          f' --evaluate SKILL {evaluateSkillPt}'\
          f' --relation NONE 0 {relation} --outing-relation {outingRelation}'\
          \
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp1} --motivation {motivation}'\
          f' --hp-keep {hpKeep1} --risk {risk1}'\
          f' --stamp {stamp1} --full-power {fullPower1} --cook-threshold {cookThreshold1}'\
          f' --cook-pt-limit {cookPtLimit1} --cook-pt-required {cookPtRequired1}'\
          \
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp2} --motivation {motivation}'\
          f' --hp-keep {hpKeep2} --risk {risk2}'\
          f' --stamp {stamp2} --full-power {fullPower2} --cook-threshold {cookThreshold2}'\
          f' --cook-pt-limit {cookPtLimit2} --cook-pt-required {cookPtRequired2}'\
          \
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp1} --motivation {motivation}'\
          f' --hp-keep {hpKeep3} --risk {risk3}'\
          f' --stamp {stamp3} --full-power {fullPower3} --cook-threshold {cookThreshold3}'\
          f' --cook-pt-limit 999999 --cook-pt-required {cookPtRequired3}'\
          f''

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

study = optuna.create_study(
    study_name='cook_s1p1g2w1_2',
    storage='sqlite:///optuna_study_cook.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=10000)
