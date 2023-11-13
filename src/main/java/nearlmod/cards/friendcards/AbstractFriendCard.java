package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.orbs.AbstractFriend;
import nearlmod.patches.NearlTags;

public abstract class AbstractFriendCard extends AbstractNearlCard {
    public static final String BG_IMG = "images/512/bg_friend_test.png";
    public String baseDescription;

    public AbstractFriendCard(String id, String name, String img, int cost, String rawDescription,
                              AbstractCard.CardType type, AbstractCard.CardColor color,
                              AbstractCard.CardRarity rarity, AbstractCard.CardTarget target, String belongFriend) {
        super(id, name, img, cost, rawDescription + " NL 虚无 。 NL 消耗 。",
                type, color, rarity, target);
        baseDescription = rawDescription;
        tags.add(NearlTags.IS_FRIEND_CARD);
        exhaust = true;
        isEthereal = true;
        textureBackgroundSmallImg = BG_IMG;
        this.belongFriend = belongFriend;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        applyFriendPower();
    }
    public void applyFriendPower() {
        magicNumber = baseMagicNumber;
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || p.orbs == null) return;
        for (AbstractOrb orb : p.orbs) {
            if (orb instanceof AbstractFriend)
                if (orb.ID.equals(belongFriend))
                    magicNumber += orb.passiveAmount;
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public abstract void upgrade();

    @Override
    public abstract void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster);
}
