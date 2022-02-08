import subprocess
import optuna

def objective(trial):
    speed = trial.suggest_uniform('speed', 0.2, 2.0)
    stamina = trial.suggest_uniform('stamina', 0.2, 2.0)
    power = trial.suggest_uniform('power', 0.2, 2.0)
    guts = trial.suggest_uniform('guts', 0.2, 2.0)
    wisdom = trial.suggest_uniform('wisdom', 0.2, 2.0)
    skillPt = trial.suggest_uniform('skillPt', 0.1, 1.0)
    hp = trial.suggest_uniform('hp', 0.6, 1.5)
    relationSpeed1 = trial.suggest_uniform('relationSpeed1', 0.2, 20.0)
    relationSpeed2 = trial.suggest_uniform('relationSpeed2', 0.2, 20.0)
    relationPower = trial.suggest_uniform('relationPower', 0.2, 20.0)
    relationWisdom1 = trial.suggest_uniform('relationWisdom1', 0.2, 20.0)
    relationWisdom2 = trial.suggest_uniform('relationWisdom2', 0.2, 20.0)
    aoharuJunior = trial.suggest_uniform('aoharuJunior', 0.0, 30.0)
    aoharuClassic1 = trial.suggest_uniform('aoharuClassic1', 0.0, 30.0)
    aoharuClassic2 = trial.suggest_uniform('aoharuClassic2', 0.0, 30.0)
    aoharuSenior = trial.suggest_uniform('aoharuSenior', 0.0, 30.0)
    cmd = f'java -jar ../cli/build/libs/cli.jar --count 50000 --chara "[超特急！フルカラー特殊PP]アグネスデジタル" 5 5 --support "[迫る熱に押されて]キタサンブラック" 4 --support "[袖振り合えば福となる♪]マチカネフクキタル" 4 --support "[感謝は指先まで込めて]ファインモーション" 4 --support "[願いまでは拭わない]ナイスネイチャ" 4 --support "[幸せは曲がり角の向こう]ライスシャワー" 4 --support "[徹底管理主義]樫本理子" 4 --speed {speed} --stamina {stamina} --power {power} --guts {guts} --wisdom {wisdom} --skill-pt {skillPt} --hp {hp} --motivation 0.25 --relation SPEED 0 {relationSpeed1} --relation SPEED 1 {relationSpeed2} --relation POWER 0 {relationPower} --relation WISDOM 0 {relationWisdom1} --relation WISDOM 1 {relationWisdom2} --aoharu 24 {aoharuJunior} --aoharu 36 {aoharuClassic1} --aoharu 48 {aoharuClassic2} --aoharu-default {aoharuSenior}'
    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(float(score))
    return float(score)

study = optuna.create_study(
    study_name='test10',
    storage='sqlite:///optuna_study.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=1000)
