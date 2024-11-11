package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.DeliciousPower;
import nearlmod.powers.HoneyGingerbreadPower;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class Specialty extends AbstractFriendCard {
    public static final String ID = "nearlmod:Specialty";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/specialty.png";
    private static final int COST = 1;
    private static final int BLOCK_NUM = 7;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public Specialty() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Gummy");
        magicNumber = baseMagicNumber = BLOCK_NUM;
        tags.add(NearlTags.IS_FOOD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, magicNumber));
        if (p.stance.ID.equals(AtkStance.STANCE_ID)) {
            addToBot(new ChangeStanceAction(new DefStance()));
        } else {
            addToBot(new ChangeStanceAction(new AtkStance()));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (AbstractDungeon.player.hasPower(DeliciousPower.POWER_ID)) {
            magicNumber += AbstractDungeon.player.getPower(DeliciousPower.POWER_ID).amount;
        }
        if (AbstractDungeon.player.hasPower(HoneyGingerbreadPower.POWER_ID)) {
            magicNumber += AbstractDungeon.player.getPower(HoneyGingerbreadPower.POWER_ID).amount;
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Specialty();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BLOCK);
        }
    }
}
