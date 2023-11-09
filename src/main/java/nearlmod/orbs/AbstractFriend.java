package nearlmod.orbs;

import basemod.abstracts.CustomOrb;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.patches.NearlTags;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractFriend extends CustomOrb {

    public AbstractFriend(String ID, String NAME, int basePassiveAmount, int baseEvokeAmount, String passiveDescription, String evokeDescription, String imgPath) {
        super(ID, NAME, basePassiveAmount, baseEvokeAmount, passiveDescription, evokeDescription, imgPath);

        showEvokeValue = false;
        angle = MathUtils.random(360.0f);
        channelAnimTimer = 0.5f;
    }

    public void applyStrength(int amount) {
        passiveAmount += amount;
        ArrayList<AbstractCard> cardSet = AbstractDungeon.player.hand.group;
        Iterator it = cardSet.iterator();
        while (it.hasNext()) {
            AbstractCard card = (AbstractCard)it.next();
            if (card instanceof AbstractNearlCard) {
                AbstractNearlCard nCard = (AbstractNearlCard) card;
                if (nCard.hasTag(NearlTags.IS_FRIEND_CARD) && nCard.belongFriend.equals(ID))
                    nCard.applyFriendPower(amount);
            }
        }
    }

    @Override
    public void onEvoke() {}

    @Override
    public void applyFocus() {}

//    @Override
//    public void updateAnimation() {}

    @Override
    public void playChannelSFX() {}
}
