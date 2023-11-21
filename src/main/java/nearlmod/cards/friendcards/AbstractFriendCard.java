package nearlmod.cards.friendcards;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.orbs.AbstractFriend;
import nearlmod.patches.NearlTags;
import nearlmod.stances.AtkStance;

import java.util.Iterator;

public abstract class AbstractFriendCard extends AbstractNearlCard {
    public static final String BG_512 = "images/512/";
    public static final String BG_1024 = "images/1024/";
    public static final String ATTACK_IMG = "bg_attack_friend.png";
    public static final String SKILL_IMG = "bg_skill_friend.png";
    public static final String POWER_IMG = "bg_power_friend.png";
    public String baseDescription;
    public boolean isSecondMagicNumberUseTrust = false;
    public static final String damageSuffix = "FriendDmg";

    public AbstractFriendCard(String id, String name, String img, int cost, String rawDescription,
                              AbstractCard.CardType type, AbstractCard.CardColor color,
                              AbstractCard.CardRarity rarity, AbstractCard.CardTarget target, String belongFriend) {
        super(id, name, img, cost, rawDescription,
                type, color, rarity, target);
        baseDescription = rawDescription;
        tags.add(NearlTags.IS_FRIEND_CARD);
        exhaust = true;
        isEthereal = true;
        if (type == CardType.ATTACK) {
            textureBackgroundSmallImg = BG_512 + ATTACK_IMG;
            textureBackgroundLargeImg = BG_1024 + ATTACK_IMG;
        } else if (type == CardType.SKILL) {
            textureBackgroundSmallImg = BG_512 + SKILL_IMG;
            textureBackgroundLargeImg = BG_1024 + SKILL_IMG;
        } else if (type == CardType.POWER) {
            textureBackgroundSmallImg = BG_512 + POWER_IMG;
            textureBackgroundLargeImg = BG_1024 + POWER_IMG;
        }
        this.belongFriend = belongFriend;
    }

    @Override
    public void applyPowers() {
        applyFriendPower();
    }
    public void applyFriendPower() {
        magicNumber = baseMagicNumber;
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || p.orbs == null) return;
        for (AbstractOrb orb : p.orbs) {
            if (orb instanceof AbstractFriend)
                if (orb.ID.equals(belongFriend))
                    magicNumber += ((AbstractFriend) orb).trustAmount;
        }
        if (isSecondMagicNumberUseTrust)
            secondMagicNumber = baseSecondMagicNumber + (magicNumber - baseMagicNumber);
        isMagicNumberModified = (magicNumber != baseMagicNumber);
        isSecondMagicNumberUseTrust = (secondMagicNumber != baseSecondMagicNumber);
    }
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        applyFriendPower();
        this.isDamageModified = false;
        float tmp = (float)this.magicNumber;
        Iterator var9;
        AbstractPower p;
        for(var9 = mo.powers.iterator(); var9.hasNext(); tmp = p.atDamageReceive(tmp, this.damageTypeForTurn, this)) {
            p = (AbstractPower)var9.next();
        }

        for(var9 = mo.powers.iterator(); var9.hasNext(); tmp = p.atDamageFinalReceive(tmp, this.damageTypeForTurn, this)) {
            p = (AbstractPower)var9.next();
        }

        if (tmp < 0.0F) {
            tmp = 0.0F;
        }

        if (this.baseDamage != MathUtils.floor(tmp)) {
            this.isDamageModified = true;
        }

        this.damage = MathUtils.floor(tmp);
        magicNumber = damage;
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public abstract void upgrade();

    @Override
    public abstract void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster);

    @Override
    public void onMoveToDiscard() {
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.discardPile));
    }

    @Override
    public void triggerOnManualDiscard() {
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.discardPile));
    }

    @Override
    public void triggerOnExhaust() {
        BaseMod.MAX_HAND_SIZE--;
    }
}
