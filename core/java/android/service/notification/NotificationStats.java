/**
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.service.notification;

import android.annotation.IntDef;
import android.annotation.NonNull;
import android.annotation.Nullable;
import android.annotation.SystemApi;
import android.annotation.TestApi;
import android.app.RemoteInput;
import android.os.Parcel;
import android.os.Parcelable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @hide
 */
@TestApi
@SystemApi
public final class NotificationStats implements Parcelable {

    private boolean mSeen;
    private boolean mExpanded;
    private boolean mDirectReplied;
    private boolean mSnoozed;
    private boolean mViewedSettings;
    private boolean mInteracted;

    /** @hide */
    @IntDef(prefix = { "DISMISSAL_SURFACE_" }, value = {
            DISMISSAL_NOT_DISMISSED, DISMISSAL_OTHER, DISMISSAL_PEEK, DISMISSAL_AOD, DISMISSAL_SHADE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface DismissalSurface {}


    private @DismissalSurface int mDismissalSurface = DISMISSAL_NOT_DISMISSED;

    /**
     * Notification has not been dismissed yet.
     */
    public static final int DISMISSAL_NOT_DISMISSED = -1;
    /**
     * Notification has been dismissed from a {@link NotificationListenerService} or the app
     * itself.
     */
    public static final int DISMISSAL_OTHER = 0;
    /**
     * Notification has been dismissed while peeking.
     */
    public static final int DISMISSAL_PEEK = 1;
    /**
     * Notification has been dismissed from always on display.
     */
    public static final int DISMISSAL_AOD = 2;
    /**
     * Notification has been dismissed from the notification shade.
     */
    public static final int DISMISSAL_SHADE = 3;

    public NotificationStats() {
    }

    protected NotificationStats(Parcel in) {
        mSeen = in.readByte() != 0;
        mExpanded = in.readByte() != 0;
        mDirectReplied = in.readByte() != 0;
        mSnoozed = in.readByte() != 0;
        mViewedSettings = in.readByte() != 0;
        mInteracted = in.readByte() != 0;
        mDismissalSurface = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (mSeen ? 1 : 0));
        dest.writeByte((byte) (mExpanded ? 1 : 0));
        dest.writeByte((byte) (mDirectReplied ? 1 : 0));
        dest.writeByte((byte) (mSnoozed ? 1 : 0));
        dest.writeByte((byte) (mViewedSettings ? 1 : 0));
        dest.writeByte((byte) (mInteracted ? 1 : 0));
        dest.writeInt(mDismissalSurface);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationStats> CREATOR = new Creator<NotificationStats>() {
        @Override
        public NotificationStats createFromParcel(Parcel in) {
            return new NotificationStats(in);
        }

        @Override
        public NotificationStats[] newArray(int size) {
            return new NotificationStats[size];
        }
    };

    /**
     * Returns whether the user has seen this notification at least once.
     */
    public boolean hasSeen() {
        return mSeen;
    }

    /**
     * Records that the user as seen this notification at least once.
     */
    public void setSeen() {
        mSeen = true;
    }

    /**
     * Returns whether the user has expanded this notification at least once.
     */
    public boolean hasExpanded() {
        return mExpanded;
    }

    /**
     * Records that the user has expanded this notification at least once.
     */
    public void setExpanded() {
        mExpanded = true;
        mInteracted = true;
    }

    /**
     * Returns whether the user has replied to a notification that has a
     * {@link android.app.Notification.Action.Builder#addRemoteInput(RemoteInput) direct reply} at
     * least once.
     */
    public boolean hasDirectReplied() {
        return mDirectReplied;
    }

    /**
     * Records that the user has replied to a notification that has a
     * {@link android.app.Notification.Action.Builder#addRemoteInput(RemoteInput) direct reply}
     * at least once.
     */
    public void setDirectReplied() {
        mDirectReplied = true;
        mInteracted = true;
    }

    /**
     * Returns whether the user has snoozed this notification at least once.
     */
    public boolean hasSnoozed() {
        return mSnoozed;
    }

    /**
     * Records that the user has snoozed this notification at least once.
     */
    public void setSnoozed() {
        mSnoozed = true;
        mInteracted = true;
    }

    /**
     * Returns whether the user has viewed the in-shade settings for this notification at least
     * once.
     */
    public boolean hasViewedSettings() {
        return mViewedSettings;
    }

    /**
     * Records that the user has viewed the in-shade settings for this notification at least once.
     */
    public void setViewedSettings() {
        mViewedSettings = true;
        mInteracted = true;
    }

    /**
     * Returns whether the user has interacted with this notification beyond having viewed it.
     */
    public boolean hasInteracted() {
        return mInteracted;
    }

    /**
     * Returns from which surface the notification was dismissed.
     */
    public @DismissalSurface int getDismissalSurface() {
        return mDismissalSurface;
    }

    /**
     * Returns from which surface the notification was dismissed.
     */
    public void setDismissalSurface(@DismissalSurface int dismissalSurface) {
        mDismissalSurface = dismissalSurface;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationStats that = (NotificationStats) o;

        if (mSeen != that.mSeen) return false;
        if (mExpanded != that.mExpanded) return false;
        if (mDirectReplied != that.mDirectReplied) return false;
        if (mSnoozed != that.mSnoozed) return false;
        if (mViewedSettings != that.mViewedSettings) return false;
        if (mInteracted != that.mInteracted) return false;
        return mDismissalSurface == that.mDismissalSurface;
    }

    @Override
    public int hashCode() {
        int result = (mSeen ? 1 : 0);
        result = 31 * result + (mExpanded ? 1 : 0);
        result = 31 * result + (mDirectReplied ? 1 : 0);
        result = 31 * result + (mSnoozed ? 1 : 0);
        result = 31 * result + (mViewedSettings ? 1 : 0);
        result = 31 * result + (mInteracted ? 1 : 0);
        result = 31 * result + mDismissalSurface;
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NotificationStats{");
        sb.append("mSeen=").append(mSeen);
        sb.append(", mExpanded=").append(mExpanded);
        sb.append(", mDirectReplied=").append(mDirectReplied);
        sb.append(", mSnoozed=").append(mSnoozed);
        sb.append(", mViewedSettings=").append(mViewedSettings);
        sb.append(", mInteracted=").append(mInteracted);
        sb.append(", mDismissalSurface=").append(mDismissalSurface);
        sb.append('}');
        return sb.toString();
    }
}
