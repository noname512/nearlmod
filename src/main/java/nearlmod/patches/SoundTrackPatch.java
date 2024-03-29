package nearlmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;

import java.util.HashMap;

@SpirePatch(clz = SoundMaster.class, method = "<ctor>")
public class SoundTrackPatch {
    @SpireInsertPatch(rloc = 3, localvars = {"map"})
    public static void Insert(SoundMaster __instance, HashMap<String, Sfx> map) {
        map.put("BLOOD_KNIGHT_REBORN_NXT", new Sfx("rhinemod/audio/sound/e_skill_bldkgtswordon.mp3", false));
        map.put("BLOOD_KNIGHT_REBORN_LAS", new Sfx("rhinemod/audio/sound/e_skill_bldkgtaxeon.mp3", false));
        map.put("BLOOD_KNIGHT_SUMMON", new Sfx("rhinemod/audio/sound/e_atk_swordsummon.mp3", false));
        map.put("BLOOD_KNIGHT_SKILL", new Sfx("rhinemod/audio/sound/e_atk_bldkgtpol_n.mp3", false));
        map.put("BLOOD_KNIGHT_ATTACK", new Sfx("rhinemod/audio/sound/e_imp_bldkgtsword.mp3", false));
        map.put("CANDLE_KNIGHT_CHARGE", new Sfx("rhinemod/audio/sound/e_atk_cadkgt_s_lp.mp3", false));
        map.put("CANDLE_KNIGHT_ATTACK", new Sfx("rhinemod/audio/sound/e_atk_cadkgt_s.mp3", false));
        map.put("LAST_KHESHIG_SKILL", new Sfx("rhinemod/audio/sound/e_imp_nmekgt_s.mp3", false));
        map.put("BRAVE_THE_DARKNESS", new Sfx("rhinemod/audio/sound/b_char_brave_the_darkness.mp3", false));
    }
}
