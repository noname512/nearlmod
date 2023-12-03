package nearlmod.cards;

import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.orbs.*;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class PathOfRadiant extends AbstractNearlCard {
    public static final String ID = "nearlmod:PathOfRadiant";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/gloriouskazimierz.png";
    private static final int COST = 1;
    private static final int TRUST_AMT = 2;
    private static final int UPGRADE_PLUS_TRUST = 2;

    public PathOfRadiant() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = TRUST_AMT;
        exhaust = true;
    }

    @Override
    public boolean extraTriggered() {
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if (orb.ID.equals(Flametail.ORB_ID) || orb.ID.equals(Fartooth.ORB_ID) || orb.ID.equals(Wildmane.ORB_ID)
                    || orb.ID.equals(Ashlock.ORB_ID) || orb.ID.equals(JusticeKnight.ORB_ID))
                return true;
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance.ID.equals(DefStance.STANCE_ID))
            addToBot(new ChangeStanceAction(new AtkStance()));
        boolean extra = extraTriggered();
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if (orb instanceof AbstractFriend) {
                ((AbstractFriend) orb).upgraded = true;
                if (extra) ((AbstractFriend) orb).applyStrength(magicNumber);
                orb.updateDescription();
            }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PathOfRadiant();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_TRUST);
        }
    }
}
