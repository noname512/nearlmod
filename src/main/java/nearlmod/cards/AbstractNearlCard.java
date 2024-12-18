package nearlmod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import nearlmod.powers.DayBreakPower;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNearlCard extends CustomCard {

    public int baseSecondMagicNumber;
    public int secondMagicNumber;
    public boolean isSecondMagicNumberModified;
    public boolean upgradedSecondMagicNumber;
    public String belongFriend;

    private float rotationTimer = 0.0F;
    private int previewIndex = 0;
    public ArrayList<AbstractCard> previewList = null;


    public AbstractNearlCard(String id, String name, String img, int cost, String rawDescription,
                             AbstractCard.CardType type, AbstractCard.CardColor color,
                             AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return new ArrayList<>();
    }

    public void upgradeSecondMagicNumber(int amount) {
        baseSecondMagicNumber += amount;
        secondMagicNumber = baseSecondMagicNumber;
        upgradedSecondMagicNumber = true;
    }

    public boolean extraTriggered() {
        return false;
    }

    @Override
    public void triggerOnGlowCheck() {
        if (extraTriggered())
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        else
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    public static int staticCalcDmg(AbstractMonster m, int baseDmg, DamageInfo.DamageType type, boolean isFriendDamage) {
        if (m == null) return baseDmg;
        float tmp = (float)baseDmg;
        for (AbstractPower power : m.powers)
            tmp = power.atDamageReceive(tmp, type);
        if (m.hasPower("nearlmod:Duel") && isFriendDamage) {
            tmp = MathUtils.floor(tmp * (1 - 0.01F * m.getPower("nearlmod:Duel").amount));
        }
        for (AbstractPower power : m.powers)
            tmp = power.atDamageFinalReceive(tmp, type);
        if (tmp < 0.0F) {
            tmp = 0.0F;
        } else if (!isFriendDamage && m.currentBlock <= 0 && AbstractDungeon.player.hasPower(DayBreakPower.POWER_ID) && type == DamageInfo.DamageType.NORMAL) {
            tmp += 3;
        }
        return MathUtils.floor(tmp);
    }

    public int calculateSingleDamage(AbstractMonster m, int baseDmg, boolean isFriendDamage) {
        return staticCalcDmg(m, baseDmg, damageTypeForTurn, isFriendDamage);
    }

    public static void addSpecificCardsToReward(AbstractCard card) {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        cards.add(card);
        addSpecificCardsToReward(cards);
    }

    public static void addSpecificCardsToReward(ArrayList<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            if (!card.canUpgrade()) continue;
            for (AbstractRelic r : AbstractDungeon.player.relics)
                r.onPreviewObtainCard(card);
        }
        RewardItem item = new RewardItem();
        item.cards = cards;
        AbstractDungeon.getCurrRoom().addCardReward(item);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        card.exhaust = this.exhaust;
        card.isEthereal = this.isEthereal;
        card.retain = this.retain;
        card.selfRetain = this.selfRetain;
        card.rawDescription = this.rawDescription;
        card.initializeDescription();
        return card;
    }
    public void update() {
        super.update();
        if (previewList == null) {
            return;
        }
        if (this.hb.hovered) {
            if (this.rotationTimer <= 0.0F) {
                this.rotationTimer = 2.0F;
                this.cardsToPreview = this.previewList.get(this.previewIndex);
                if (this.previewIndex == this.previewList.size() - 1) {
                    this.previewIndex = 0;
                } else {
                    this.previewIndex++;
                }
            } else {
                this.rotationTimer -= Gdx.graphics.getDeltaTime();
            }
        }
    }

    public static class SecondMagicNumber extends DynamicVariable {

        @Override
        public String key() {
            return "nearlmod:M2";
        }

        @Override
        public boolean isModified(AbstractCard card) {
            if (card instanceof AbstractNearlCard) {
                return ((AbstractNearlCard)card).isSecondMagicNumberModified;
            } else {
                return false;
            }
        }

        @Override
        public void setIsModified(AbstractCard card, boolean v) {
            if (card instanceof AbstractNearlCard) {
                ((AbstractNearlCard)card).isSecondMagicNumberModified = v;
            }
        }

        @Override
        public int value(AbstractCard card) {
            if (card instanceof AbstractNearlCard) {
                return ((AbstractNearlCard) card).secondMagicNumber;
            } else {
                return 0;
            }
        }

        @Override
        public int baseValue(AbstractCard card) {
            if (card instanceof AbstractNearlCard) {
                return ((AbstractNearlCard) card).baseSecondMagicNumber;
            } else {
                return 0;
            }
        }

        @Override
        public boolean upgraded(AbstractCard card) {
            if (card instanceof AbstractNearlCard) {
                return ((AbstractNearlCard)card).upgradedSecondMagicNumber;
            } else {
                return false;
            }
        }
    }
}