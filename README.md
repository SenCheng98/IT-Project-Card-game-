# ITSDTeamProject-0


已实现：
移动、攻击、血量换算、击杀等指令
每回合从卡组总抽取一定数量的卡牌到手牌

遗留问题：
当卡组中的牌数量不够时，抽卡会报错
if(handCard.get(0) == null) 似乎失效

下阶段任务：
点击手卡将unit/spell召唤到tile中（手牌和unit的对应关系，点击手牌能识别出是哪个unit）
