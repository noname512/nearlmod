package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.orbs.AbstractFriend;
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

import java.util.Iterator;

public abstract class AbstractFriendCard extends AbstractNearlCard {
    public static final String BG_IMG = "images/512/bg_friend_test.png";

    public AbstractFriendCard(String id, String name, String img, int cost, String rawDescription,
                              AbstractCard.CardType type, AbstractCard.CardColor color,
                              AbstractCard.CardRarity rarity, AbstractCard.CardTarget target, String belongFriend) {
        super(id, name, img, cost, rawDescription + " NL 虚无 。 NL 消耗 。",
                type, color, rarity, target);
        tags.add(NearlTags.IS_FRIEND_CARD);
        exhaust = true;
        isEthereal = true;
        textureBackgroundSmallImg = BG_IMG;
        this.belongFriend = belongFriend;
    }

    public void updateDmg() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || p.orbs == null) return;
        for (Iterator it = p.orbs.iterator(); it.hasNext();) {
            AbstractOrb orb = (AbstractOrb)it.next();
            if (orb instanceof AbstractFriend)
                if (orb.ID.equals(belongFriend))
                    magicNumber += ((AbstractFriend)orb).passiveAmount;
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public void applyFriendPower(int amount) {
        magicNumber += amount;
        isMagicNumberModified = true;
    }

    @Override
    public abstract void upgrade();

    @Override
    public abstract void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster);
}
