package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.patches.AbstractCardEnum;

import static nearlmod.patches.NearlTags.IS_KNIGHT_CARD;

public class JusticeDrive extends AbstractFriendCard {
    public static final String ID = "nearlmod:JusticeDrive";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/justicedrive.png";
    private static final int COST = 0;
    private static final int VULN_GAIN = 1;
    private static final int UPGRADE_PLUS_VULN = 1;
    private static final int DAMAGE = 2;

    public JusticeDrive() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ALL_ENEMY, "nearlmod:JusticeKnight");
        secondMagicNumber = baseSecondMagicNumber = VULN_GAIN;
        magicNumber = DAMAGE;
        tags.add(IS_KNIGHT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PureDamageAllEnemiesAction(p, magicNumber, belongFriend + damageSuffix));
        for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters)
            if (!ms.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(ms, p, new VulnerablePower(ms, secondMagicNumber, false)));
                if (ms.hasPower("Flight"))
                    addToBot(new RemoveSpecificPowerAction(ms, p, "Flight"));
            }
    }

    @Override
    public AbstractCard makeCopy() {
        return new JusticeDrive();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_VULN);
        }
    }
}
