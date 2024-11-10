package nearlmod.cards.friendcards;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.orbs.AbstractFriend;

public abstract class AbstractFriendCard extends AbstractNearlCard {
    public String baseDescription;
    public boolean isSecondMagicNumberUseTrust = false;
    public static final String damageSuffix = "FriendDmg";

    public AbstractFriendCard(String id, String name, String img, int cost, String rawDescription,
                              AbstractCard.CardType type, AbstractCard.CardColor color,
                              AbstractCard.CardRarity rarity, AbstractCard.CardTarget target, String belongFriend) {
        super(id, name, img, cost, rawDescription,
                type, color, rarity, target);
        baseDescription = rawDescription;
        exhaust = true;
        isEthereal = true;
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
                    magicNumber += ((AbstractFriend) orb).getTrustAmount();
        }
        if (isSecondMagicNumberUseTrust)
            secondMagicNumber = baseSecondMagicNumber + (magicNumber - baseMagicNumber);
        isMagicNumberModified = (magicNumber != baseMagicNumber);
        isSecondMagicNumberModified = (secondMagicNumber != baseSecondMagicNumber);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        applyPowers();
        if (this.type == CardType.ATTACK)
            magicNumber = calculateSingleDamage(mo, magicNumber, true);
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

    public void setUniqueUsed(String friend) {
        for (AbstractOrb o : AbstractDungeon.player.orbs) {
            if ((o instanceof AbstractFriend) && (o.ID.equals(friend))) {
                ((AbstractFriend) o).uniqueUsed = true;
                return;
            }
        }
    }
}
