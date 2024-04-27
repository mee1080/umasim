import subprocess
import optuna

def objective(trial):

    speed = 1.0
    stamina = trial.suggest_float ('stamina', 0.5, 1.5, step=0.1)
    power = trial.suggest_float ('power', 0.5, 1.5, step=0.1)
    guts = trial.suggest_float ('guts', 0.5, 1.5, step=0.1)
    wisdom = trial.suggest_float ('wisdom', 0.5, 1.5, step=0.1)
    skillPt = trial.suggest_float ('skillPt', 0.5, 1.5, step=0.1)

    hp1 = trial.suggest_float ('hp1', 0.4, 2.0, step=0.1)
    hp2 = trial.suggest_float ('hp2', 0.4, 2.0, step=0.1)
    hp3 = trial.suggest_float ('hp3', 0.4, 2.0, step=0.1)
    motivation = trial.suggest_float ('motivation', 10.0, 20.0, step=1.0)
    relation = trial.suggest_float ('relation', 5.0, 20.0, step=1.0)
    hpKeep1 = trial.suggest_float ('hpKeep1', 0.0, 2.0, step=0.1)
    hpKeep2 = trial.suggest_float ('hpKeep2', 0.0, 2.0, step=0.1)
    hpKeep3 = trial.suggest_float ('hpKeep3', 0.0, 2.0, step=0.1)
    risk1 = trial.suggest_float ('risk1', 1.0, 10.0, step=0.2)
    risk2 = trial.suggest_float ('risk2', 1.0, 10.0, step=0.2)
    risk3 = trial.suggest_float ('risk3', 1.0, 10.0, step=0.2)

    athleticBase1 = trial.suggest_float ('athleticBase1', 1.0, 5.0, step=0.2)
    athleticBase2 = trial.suggest_float ('athleticBase2', 1.0, 5.0, step=0.2)
    athleticBase3 = trial.suggest_float ('athleticBase3', 1.0, 5.0, step=0.2)
    athleticRequired1 = trial.suggest_float ('athleticRequired1', 1.0, 5.0, step=0.2)
    athleticRequired2 = trial.suggest_float ('athleticRequired2', 1.0, 5.0, step=0.2)
    athleticRequired3 = trial.suggest_float ('athleticRequired3', 1.0, 5.0, step=0.2)
    athleticBonus1 = trial.suggest_float ('athleticBonus1', 20.0, 60.0, step=5.0)
    athleticBonus2 = trial.suggest_float ('athleticBonus2', 20.0, 60.0, step=5.0)
    athleticBonus3 = trial.suggest_float ('athleticBonus3', 20.0, 60.0, step=5.0)

    consultMin = trial.suggest_float ('consultMin', 10.0, 40.0, step=2.0)
    consultAthleticRequired = trial.suggest_float ('consultAthleticRequired', 0.0, 5.0, step=0.2)
    consultHeatUpStatus = trial.suggest_float ('consultHeatUpStatus', 0.0, 5.0, step=0.2)
    keepRedHeatUp = trial.suggest_float ('keepRedHeatUp', 1.0, 1.5, step=0.05)

    """
    cmd = f'java -jar ../cli/build/libs/cli.jar --data-dir ../data'\
          f' --count 50000 --scenario UAF'\
          f' --chara "[プラタナス・ウィッチ]スイープトウショウ" 5 5'\
          f' --support "[血脈の胎動]ドゥラメンテ" 4'\
          f' --support "[大望は飛んでいく]エルコンドルパサー" 4'\
          f' --support "[冬溶かす熾火]メジロラモーヌ" 4'\
          f' --support "[只、君臨す。]オルフェーヴル" 4'\
          f' --support "[かっとばせー！ですわ！？]メジロマックイーン" 4'\
          f' --support "[共に描くキラメキ]都留岐涼花" 4'\
          f' --factor SPEED 3 --factor STAMINA 3 --factor POWER 3'\
          f' --factor POWER 3 --factor POWER 3 --factor POWER 3'\
          f' --evaluate SPEED 1.1 2200'\
          f' --evaluate STAMINA 1.2 900'\
          f' --evaluate POWER 1.1 1800'\
          f' --evaluate GUTS 1.2 1200'\
          f' --evaluate WISDOM 1.1 1400'\
          f' --relation NONE 0 {relation}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp1} --motivation {motivation}'\
          f' --hp-keep {hpKeep1} --risk {risk1}'\
          f' --athletic-base {athleticBase1} --athletic-required {athleticRequired1}'\
          f' --athletic-bonus {athleticBonus1} --consult-min {consultMin}'\
          f' --consult-athletic-required {consultAthleticRequired} --consult-heat-up-status {consultHeatUpStatus}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp2} --motivation {motivation}'\
          f' --hp-keep {hpKeep2} --risk {risk2}'\
          f' --athletic-base {athleticBase2} --athletic-required {athleticRequired2}'\
          f' --athletic-bonus {athleticBonus2} --consult-min {consultMin}'\
          f' --consult-athletic-required {consultAthleticRequired} --consult-heat-up-status {consultHeatUpStatus}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp1} --motivation {motivation}'\
          f' --hp-keep {hpKeep3} --risk {risk3}'\
          f' --athletic-base {athleticBase3} --athletic-required {athleticRequired3}'\
          f' --athletic-bonus {athleticBonus3} --consult-min {consultMin}'\
          f' --consult-athletic-required {consultAthleticRequired} --consult-heat-up-status {consultHeatUpStatus}'\
          f''
    """

    cmd = f'java -Dfile.encoding=UTF-8 -jar ../cli/build/libs/cli.jar --data-dir ../data'\
          f' --count 50000 --scenario UAF'\
          f' --chara "[清らに星澄むスノーロリィタ]メジロブライト" 5 5'\
          f' --support "[血脈の胎動]ドゥラメンテ" 4'\
          f' --support "[大望は飛んでいく]エルコンドルパサー" 4'\
          f' --support "[大地と我らのアンサンブル]サウンズオブアース" 4'\
          f' --support "[只、君臨す。]オルフェーヴル" 4'\
          f' --support "[君と見る泡沫]マンハッタンカフェ" 4'\
          f' --support "[共に描くキラメキ]都留岐涼花" 4'\
          f' --factor STAMINA 3 --factor STAMINA 3 --factor POWER 3'\
          f' --factor POWER 3 --factor POWER 3 --factor POWER 3'\
          f' --evaluate SPEED 1.1 2200'\
          f' --evaluate STAMINA 1.15 1600'\
          f' --evaluate POWER 1.1 1500'\
          f' --evaluate GUTS 1.2 1200'\
          f' --evaluate WISDOM 1.1 1300'\
          f' --evaluate SKILL 1.0 4000'\
          f' --relation NONE 0 {relation}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp1} --motivation {motivation}'\
          f' --hp-keep {hpKeep1} --risk {risk1}'\
          f' --athletic-base {athleticBase1} --athletic-required {athleticRequired1}'\
          f' --athletic-bonus {athleticBonus1} --consult-min {consultMin}'\
          f' --consult-athletic-required {consultAthleticRequired} --consult-heat-up-status {consultHeatUpStatus}'\
          f' --keep-red-heat-up {keepRedHeatUp}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp2} --motivation {motivation}'\
          f' --hp-keep {hpKeep2} --risk {risk2}'\
          f' --athletic-base {athleticBase2} --athletic-required {athleticRequired2}'\
          f' --athletic-bonus {athleticBonus2} --consult-min {consultMin}'\
          f' --consult-athletic-required {consultAthleticRequired} --consult-heat-up-status {consultHeatUpStatus}'\
          f' --keep-red-heat-up {keepRedHeatUp}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp1} --motivation {motivation}'\
          f' --hp-keep {hpKeep3} --risk {risk3}'\
          f' --athletic-base {athleticBase3} --athletic-required {athleticRequired3}'\
          f' --athletic-bonus {athleticBonus3} --consult-min {consultMin}'\
          f' --consult-athletic-required {consultAthleticRequired} --consult-heat-up-status {consultHeatUpStatus}'\
          f' --keep-red-heat-up {keepRedHeatUp}'\
          f''

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

study = optuna.create_study(
    study_name='uaf_s2h1g1w1_2',
    storage='sqlite:///optuna_study_uaf.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=10000)
