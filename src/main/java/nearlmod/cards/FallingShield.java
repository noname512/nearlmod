package nearlmod.cards;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import nearlmod.NLMOD;
import nearlmod.actions.FallingShieldAction;
import nearlmod.characters.Nearl;
import nearlmod.orbs.Horn;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FallingShield extends AbstractNearlCard {
    public static final String ID = "nearlmod:FallingShield";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/fallingshield.png";
    private static final int COST = -1;
    private static final int ATTACK_TIMES = 2;
    private static final int EXTRA_TIMES = 2;
    private static final int UPGRADE_PLUS_TIME = 1;
    private static final Logger logger = LogManager.getLogger(NLMOD.class.getName());

    public FallingShield() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = ATTACK_TIMES;
        secondMagicNumber = baseSecondMagicNumber = EXTRA_TIMES;
        tags.add(NearlTags.IS_SUMMON_CARD);
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Horn.ORB_ID;

        previewList = Nearl.getFriendCard(Horn.ORB_ID);
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Horn.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int times = magicNumber;
        int tempHp = TempHPField.tempHp.get(p);
        baseDamage = tempHp;
        boolean summon = false;
        if (p.hasRelic(ChemicalX.ID)) {
            baseDamage += 2;
            p.getRelic(ChemicalX.ID).flash();
        }
        calculateCardDamage(m);
        if (extraTriggered()) {
            times += secondMagicNumber;
        } else {
            summon = true;
        }
        if (isInAutoplay) {
            tempHp = 0;
        }
        if (AbstractDungeon.player != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.player.hasPower("FreeAttackPower") && this.type == AbstractCard.CardType.ATTACK) {
            tempHp = 0;
        }
        // TODO: 目前无法相应死灵书等，考虑下要不要干脆改描述算了（例如消耗你所有的临时生命，造成等量伤害2次，如果号角在场，再造成2次，否则召唤，类似于这种写法）
        // TODO: 感觉这个bug是修不完的。直接破坏原有逻辑了（或者大力patch，还是算了吧）
        addToBot(new FallingShieldAction(tempHp, damage, times, summon, m));
        costForTurn = 0;
        energyOnUse = 0;
    }

    @Override
    public AbstractCard makeCopy() {
        return new FallingShield();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_TIME);
            initializeDescription();
        }
    }
}
