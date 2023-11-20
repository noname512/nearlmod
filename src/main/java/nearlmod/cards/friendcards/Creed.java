package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class Creed extends AbstractFriendCard {
    public static final String ID = "nearlmod:Creed";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/creed.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 8;
    private static final int UPGRADE_PLUS_DMG = 3;

    public Creed() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.ENEMY, "nearlmod:Shining");
        magicNumber = baseMagicNumber = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo info = new DamageInfo(p, magicNumber);
        info.name = belongFriend + AbstractFriendCard.damageSuffix;
        addToBot(new DamageAction(m, info));
        if (p.stance.ID.equals(AtkStance.STANCE_ID)) {
            addToBot(new ChangeStanceAction(new DefStance()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Creed();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DMG);
        }
    }
}
